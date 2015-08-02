(ns ring-ttl-session.core
  (:require [clojure.core.cache :refer [ttl-cache-factory]]
            [ring.middleware.session.store :refer [SessionStore]]))

(deftype TTLMemoryStore [cache-atom]
  SessionStore
  (read-session [_ k]
    (when-let [session (get @cache-atom k)]
      (swap! cache-atom assoc k session)
      session))
  (write-session [_ k data]
    (let [k (or k (str (java.util.UUID/randomUUID)))]
      (swap! cache-atom assoc k data)
      k))
  (delete-session [_ k]
    (swap! cache-atom dissoc k)
    nil))

(defn ttl-memory-store
  "Returns an implementation of SessionStore where sessions have a time-to-live given
  in seconds."
  [ttl]
  (TTLMemoryStore. (atom (ttl-cache-factory {} :ttl (* 1000 ttl)))))
