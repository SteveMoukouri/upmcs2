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

(defn tick-received
  []
  (println "Tick!")
)

(def embla-signals
  {:tick [#(tick-received)]
   :stop 1
   }
)

(defn subscribe 
  ;; Subscription mechanism.
  ;; Subscribe to a signal (either user-defined or core)
  ;; and supply a function that will treat this signal, to be called
  ;; every time the signal is emitted.
  ;; TODO: Some kind of arity check?
  [signame sigfun]
  (if (nil? (get embla-signals signame))
    (println "No signal matching " signame)
    (do 
      (assoc (embla-signals signame (cons sigfun (get embla-signals signame))))
      (println "Matching signal" signame)
    )
  )
  )

(defn execute [fun args]
  (apply fun args)
  )

(defn emit
  ;; Emitting a signal: reception is done by subscribing to the signal.
  ;; Reaction to the signal is defined by the subscribed function.
  ;; TODO: check for function arity?
  
  [signame & sigargs]
  (let [funlist (get embla-signals signame)]
    (if (nil? funlist)
      (println "Error: no signal matching " signame)
      (do 
        (if (nil? sigargs)
          ;; No arguments for signal
          (doseq [f funlist] (f))
          ;; Apply argument list for signal functions
          (doseq [f funlist] (f))
          )
        )
      )
    )
  )

(defn gameloop 
  ;; The gameloop. Set up stuff if first launch, else keep sending 'tick' until
  ;; close time.
  ;; TODO: How the fuck do we shut the loop?
  [] 
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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, Embla!")
  (println "Beginning gameloop...")
  (gameloop)
  )
