(ns embla.signals
  (:require [clojure.core.async 
             :as async
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]])
  (:gen-class))

(defn combine1 
  "Generates an async channel listening to signal, applying func
  to the message and sending it"
  [func signal]
  (let [channel (chan)]
    (go (let [msg (<! signal)] (func msg)))
    chan 
    ))

(defn combine
  "Generates an async channel listening for signals
  and applying func to their outputs"
  ;; Buffering the messages from the signal list would be nice.
  ;; As it is, only the latest is used.
  [func & signals]
  ;; Create signal list//ID by place in list

  (def signal-count (atom 0))
  ;; Output channel, entry signal number, 
  ;; signal list [msg signal signal-localid]
  (let [signal-out (chan)
        siglist-size (count signals)
        ;; Vecteur [atom() signal], atom à filer à go
        ;; TODO: check.
        siglist (map (fn [s] ([s (atom nil)])) signals)]
    ;; Define a waiting function for each sig in the argument list
    ;; => (count signals) waiting processes
    (loop [loop-list siglist]
      (if (empty? loop-list)
        (chan))
      (let [[sig & sigs-recur] loop-list]
        (go 
          (let [msg (<! (second sig))
                storage (first sig)]
            ;; if msg's first arrival, incr signal-count
            (if (nil?)
              (swap! signal-count incr))
            ;; Put signal in its place either way
            (swap! storage (fn [x] msg))
            ;; If the signal has sufficient arguments, send it.
            (if (= @signal-count siglist-size)
              (>! signal-out (apply func (map siglist (fn [x] (@(first x)))))))))
        (recur sigs-recur)))))
