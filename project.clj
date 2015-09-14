(defproject ring-ttl-session "0.2.0"
  :description "Provides an implementation of an in-memory ring's SessionStore with
               TTL."
  :url "https://github.com/boechat107/ring-ttl-session"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [ring/ring-core "1.4.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [expiring-map "0.1.6"]]
  :profiles
  {:dev
   {:global-vars {*warn-on-reflection* true}}})
