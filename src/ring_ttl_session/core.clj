(ns ring-ttl-session.core
  (:require [clojure.core.cache :refer [ttl-cache-factory]]
            [ring.middleware.session.store :refer [SessionStore]]
            [expiring-map.core :as em]))

(defn- unique-id []
  (str (java.util.UUID/randomUUID)))

(deftype TTLMemoryStore [cache-atom]
  SessionStore
  (read-session [_ k]
    (when-let [session (get @cache-atom k)]
      (swap! cache-atom assoc k session)
      session))
  (write-session [_ k data]
    (let [k (or k (unique-id))]
      (swap! cache-atom assoc k data)
      k))
  (delete-session [_ k]
    (swap! cache-atom dissoc k)
    nil))

(deftype ExpiringMapStore [em-map]
  SessionStore
  (read-session [_ k]
    (get em-map k nil))
  (write-session [_ k data]
    (let [k (or k (unique-id))]
      (em/assoc! em-map k data)
      k))
  (delete-session [_ k]
    (em/dissoc! em-map k)
    nil))

(defn ttl-memory-store
  "Returns an implementation of SessionStore where sessions have a time-to-live given
  in seconds.
  By default, the returned session store is based on expiring-map. Expiring-map
  options (like listeners and expiration policy) can be given as an optional map.
  If an optional key :core-cache is given, a session stored based on core.cache is
  returned."
  [ttl & [opts]]
  (cond
    (= opts :core-cache) 
    (TTLMemoryStore. (atom (ttl-cache-factory {} :ttl (* 1000 ttl))))
    ;;
    (or (nil? opts) (map? opts)) 
    (ExpiringMapStore. 
      (em/expiring-map ttl (merge opts {:expiration-policy :access})))
    :else (throw (Exception. "Unknown implementation option"))))
