(ns ring-ttl-session.core-test
  (:require [clojure.test :refer :all]
            [ring.middleware.session.store :refer :all]
            [ring-ttl-session.core :refer :all]))

(deftest memory-session-read-not-exist
  (let [store (ttl-memory-store 1)]
    (is (nil? (read-session store "non-existent")))))

(deftest memory-session-create
  (let [store (ttl-memory-store 2)
        skey (write-session store nil {:ani "dog"})]
    (is (not (nil? skey)))
    (is (= {:ani "dog"}
           (read-session store skey)))
    (testing "Keeping session alive"
      (Thread/sleep 1000)
      (is (= {:ani "dog"}
             (read-session store skey)))
      (Thread/sleep (* 1.5 1000))
      (is (= {:ani "dog"}
             (read-session store skey))))
    (testing "Expired session"
      (Thread/sleep (* 2.5 1000))
      (is (nil? (read-session store skey))))))

(deftest memory-session-update
  (let [store (ttl-memory-store 2)
        skey (write-session store nil {:veg "tree"})
        skey* (write-session store skey {:veg "leaf"})]
    (is (= skey skey*))
    (is (= (read-session store skey)
           {:veg "leaf"}))
    (Thread/sleep (* 2.5 1000))
    (is (nil? (read-session store skey)))))

(deftest memory-session-delete
  (let [store (ttl-memory-store 1)
        skey (write-session store nil {:min "salt"})]
    (is (nil? (delete-session store skey)))
    (is (nil? (read-session store skey)))))
