(defproject ring-ttl-session "0.3.0"
  :description "Provides an implementation of an in-memory ring's SessionStore with
               TTL."
  :url "https://github.com/boechat107/ring-ttl-session"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[ring/ring-core "1.6.1"]
                 [org.clojure/core.cache "0.6.5"]
                 [expiring-map "0.1.8"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}}
  :aliases {"test" ["test" ":only" "ring-ttl-session.core-test"]
            "all" ["with-profile" "+dev:+1.7:+1.6"]}
  )
