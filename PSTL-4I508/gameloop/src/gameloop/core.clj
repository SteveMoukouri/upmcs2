(ns gameloop.core
  (:gen-class))

(defn arity_match [function matching]
  ;; In case we want to check function arity within the signal list.
  ;; Unused atm
  (let [argCounts (->> function meta :arglists (map count))]
    (loop [args argCounts]
      (if (empty? list)
        false
        (let [[argc & remaining-argcs] args]
          (if (= matching argc)
            true
            (recur remaining-argcs)
            )
          )
        )
      )
    )
  )

(def _signals_
  {:tick []
   :stop [] 
   }
)

(defn subscribe 
  ;; Subscription mechanism.
  ;; Subscribe to a signal (either user-defined or core)
  ;; and supply a function that will treat this signal, to be called
  ;; every time the signal is emitted.
  ;; TODO: Some kind of arity check?
  [signame sigfun]
  (if (nil? (get _signals_ signame))
    (println "No signal matching " signame)
    (assoc (_signals_ signame (cons sigfun (get _signals_ signame))))
    )
  )

(defn emit
  ;; Emitting a signal: reception is done by subscribing to the signal.
  ;; Reaction to the signal is defined by the subscribed function.
  ;; TODO: check for function arity?
  
  [signame & sigargs]
  (if 
      (nil? (get _signals_ signame))
    (print "Error: no signal matching " signame)
    (map (fn [function] (apply function sigargs)) (get _signals_ signame))
    )
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;; SHORT AND UNASSUMING BUT CAPITAL ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn gameloop 
  ;; The gameloop. Set up stuff if first launch, else keep sending 'tick' until
  ;; close time.
  ;; TODO: How the fuck do we shut the loop?
  ([] 
   (def time-last-tick (atom 0))
   (while true
     (let [current (System/currentTimeMillis)]
       (let [delta-t (- current @time-last-tick)]
         (if (> delta-t 1000)
           (do
             (emit :tick)
             (swap! time-last-tick (fn [x] current))
             )
           ()
           )
         )
       )
     )
   )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, Embla!")
  (println "Beginning gameloop...")
  (gameloop)
  )

