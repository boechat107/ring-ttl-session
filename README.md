# ring-ttl-session

A session storage that stores session data in-memory with a time-to-live (TTL).
It's very similar to 
[ring.middleware.session.memory](https://github.com/ring-clojure/ring/wiki/Sessions),
but it uses [core.cache](https://github.com/clojure/core.cache) instead of a
Clojure's native map.

## Installation

## Usage

The difference from the Ring's native 
[in-memory session store](https://github.com/ring-clojure/ring/wiki/Sessions#session-stores)
is minimal.

```clojure
(require '[ring.middleware.session :refer [wrap-session]]
         '[ring-ttl-session.core :refer [ttl-memory-store]])

(def app
  (wrap-session handler {:store (ttl-memory-store (* 60 30))}))
```

The argument of `ttl-memory-store` is the expiration time given in seconds
(the example's session expires in 30 minutes).

## License

Copyright © 2015 Andre Boechat

Distributed under the Eclipse Public License, the same as Clojure.
