(ns embla.signals
  (:require [clojure.core.async 
             :as async
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]])
  (:gen-class))

(defn combine1
    ;; ('a -> 'b) -> signal 'a -> signal 'b
  [func signal]
  (let [channel (chan)]
    (go (let [msg (<! signal)] (func msg)))
    chan))

(defn combine
  "Generates an async channel listening for signals
  and applying func to their outputs"
  [func & signals]
  (def signal-count (atom 0))
  (let [signal-out (chan)
        siglist-size (count signals)
        ;; [atom signal] vector, pass atom to go for each signal
        siglist (map (fn [s] ([(atom nil) s])) signals)]
    ;; Define a waiting function for each sig in the argument list
    ;; => (count signals) waiting processes
    (loop [loop-list siglist]
      (if (empty? loop-list)
        signal-out)
      (let [[sig & sigs-recur] loop-list]
        (go 
          (let [msg (<! (second sig))
                storage (first sig)]
            ;; if msg's first arrival, inc signal-count
            (if (nil? storage)
              (swap! signal-count inc))
            ;; Put signal in its place either way
            (swap! storage (fn [x] msg))
            ;; If the signal has sufficient arguments, send it.
            (if (= @signal-count siglist-size)
              (>! signal-out (apply func (map siglist (fn [x] (@(first x)))))))))
        (recur sigs-recur)))))
