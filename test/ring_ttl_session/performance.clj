(ns ring-ttl-session.performance
  (:require [ring.middleware.session.store :refer :all]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring-ttl-session.core :refer [ttl-memory-store]]
            [criterium.core :refer [bench quick-bench]]))

(defn check-nonexistent-read []
  ;; Results:
  ;; 
  ;; Evaluation count : 2097924 in 6 samples of 349654 calls.
  ;; Execution time mean : 276.700909 ns
  ;; Execution time std-deviation : 4.436926 ns
  ;; Execution time lower quantile : 270.910014 ns ( 2.5%)
  ;; Execution time upper quantile : 282.034356 ns (97.5%)
  ;; Overhead used : 13.799339 ns

  ;; Evaluation count : 10700184 in 6 samples of 1783364 calls.
  ;; Execution time mean : 44.712275 ns
  ;; Execution time std-deviation : 2.904135 ns
  ;; Execution time lower quantile : 42.764193 ns ( 2.5%)
  ;; Execution time upper quantile : 49.585376 ns (97.5%)
  ;; Overhead used : 13.799339 ns 
  (let [ttl (ttl-memory-store 10)
        mem (memory-store)]
    (quick-bench (read-session ttl "nonexistent"))
    (quick-bench (read-session mem "nonexistent"))))

(defn check-session-read []
  ;; Results:
  ;; 
  ;; Evaluation count : 260016 in 6 samples of 43336 calls.
  ;; Execution time mean : 2.301301 µs
  ;; Execution time std-deviation : 61.142398 ns
  ;; Execution time lower quantile : 2.210768 µs ( 2.5%)
  ;; Execution time upper quantile : 2.368702 µs (97.5%)
  ;; Overhead used : 13.799339 ns
  ;; 
  ;; Evaluation count : 8550792 in 6 samples of 1425132 calls.
  ;; Execution time mean : 57.984783 ns
  ;; Execution time std-deviation : 0.741526 ns
  ;; Execution time lower quantile : 56.573950 ns ( 2.5%)
  ;; Execution time upper quantile : 58.594382 ns (97.5%)
  ;; Overhead used : 13.799339 ns
  (let [ttl (ttl-memory-store 10)
        mem (memory-store)
        data {:foo "bar"}
        ttl-key (write-session ttl nil data)
        mem-key (write-session mem nil data)]
    (quick-bench (read-session ttl ttl-key))
    (quick-bench (read-session mem mem-key))))

(defn check-session-create []
  ;; Results:
  ;; 
  ;; Evaluation count : 324 in 6 samples of 54 calls.
  ;; Execution time mean : 1.749771 ms
  ;; Execution time std-deviation : 47.279785 µs
  ;; Execution time lower quantile : 1.690888 ms ( 2.5%)
  ;; Execution time upper quantile : 1.799542 ms (97.5%)
  ;; Overhead used : 13.799339 ns

  ;; Evaluation count : 104730 in 6 samples of 17455 calls.
  ;; Execution time mean : 5.809839 µs
  ;; Execution time std-deviation : 64.649296 ns
  ;; Execution time lower quantile : 5.732449 µs ( 2.5%)
  ;; Execution time upper quantile : 5.892600 µs (97.5%)
  ;; Overhead used : 13.799339 ns
  (let [ttl (ttl-memory-store 10)
        mem (memory-store)
        data {:foo "bar"}]
    (quick-bench (write-session ttl nil data))
    (quick-bench (write-session mem nil data))))
