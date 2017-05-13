# ring-ttl-session

![travis-status](https://travis-ci.org/boechat107/ring-ttl-session.svg)

A session storage that stores session data in-memory with a time-to-live (TTL).
It's very similar to 
[ring.middleware.session.memory](https://github.com/ring-clojure/ring/wiki/Sessions),
but it may use [expiring-map](https://github.com/yogthos/expiring-map)
(default) or [core.cache](https://github.com/clojure/core.cache) instead of
a Clojure's native map.

## Installation

[![Clojars Project](http://clojars.org/ring-ttl-session/latest-version.svg)](http://clojars.org/ring-ttl-session)

## Usage

The difference from the Ring's native 
[in-memory session store](https://github.com/ring-clojure/ring/wiki/Sessions#session-stores)
is minimal.

```clojure
(require '[ring.middleware.session :refer [wrap-session]]
         '[ring-ttl-session.core :refer [ttl-memory-store]])

(def app
  ;; Using the default implementation, expiring-map.
  (wrap-session handler {:store (ttl-memory-store (* 60 30))}))

;; Using core.cache
;; (ttl-memory-store (* 60 30) :core-cache)
```

The argument of `ttl-memory-store` is the expiration time given in seconds
(the example's session expires in 30 minutes). At least for now, it's
recommended to use the default implementation (expiring-map) because of it's
low performance overhead when compared with the bare in-memory session store 
(check the [`ring-ttl-session.performance` namespace](https://github.com/boechat107/ring-ttl-session/blob/develop/test/ring_ttl_session/performance.clj)
or [this issue](https://github.com/boechat107/ring-ttl-session/issues/2)
for details).

### Listeners

Another interesting feature of `expiring-map`, the default implementation, is
the support for listeners.

```clojure
(ttl-memory-store (* 60 30) {:listeners [(fn [k v] (println k v))]})
```

Other supported features and options can be checked in the 
[project's page](https://github.com/yogthos/expiring-map).

## License

Copyright © 2015 Andre Boechat

Distributed under the Eclipse Public License, the same as Clojure.
