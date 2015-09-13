(ns ring-ttl-session.core-test
  (:require [clojure.test :refer :all]
            [ring.middleware.session.store :refer :all]
            [ring-ttl-session.core :refer :all]))

(defn read-not-exist
  [store-opt]
  (let [store (ttl-memory-store 1 store-opt)]
    (is (nil? (read-session store "non-existent")))))

(defn create-session
  [store-opt]
  (let [store (ttl-memory-store 2 store-opt)
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

(defn update-session
  [store-opt]
  (let [store (ttl-memory-store 2 store-opt)
        skey (write-session store nil {:veg "tree"})
        skey* (write-session store skey {:veg "leaf"})]
    (is (= skey skey*))
    (is (= (read-session store skey)
           {:veg "leaf"}))
    (Thread/sleep (* 2.5 1000))
    (is (nil? (read-session store skey)))))

(defn del-session
  [store-opt]
  (let [store (ttl-memory-store 1 store-opt)
        skey (write-session store nil {:min "salt"})]
    (is (nil? (delete-session store skey)))
    (is (nil? (read-session store skey)))))

(deftest default-read-not-exit
  (read-not-exist nil))

(deftest core-cache-read-not-exit
  (read-not-exist :core-cache))

(deftest default-session-creation
  (create-session nil))

(deftest core-cache-session-creation
  (create-session :core-cache))

(deftest default-session-update
  (update-session nil))

(deftest core-cache-session-update
  (update-session :core-cache))

(deftest default-session-delete
  (del-session nil))

(deftest core-cache-session-delete
  (del-session :core-cache))
