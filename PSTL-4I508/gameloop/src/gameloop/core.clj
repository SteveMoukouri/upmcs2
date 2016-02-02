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
            (recur remaining-argcs)))))))

(defn sigfun-name
  "Get the name of sigfun."
  [sigfun]
  (first sigfun))

(defn sigfun-priority
  "Get the priority of sigfun."
  [sigfun]
  (nth sigfun 1))

(defn sigfun-arity
  "Get the arity of sigfun."
  [sigfun]
  (nth sigfun 2))

(defn sigfun-function
  "Get the function of sigfun."
  [sigfun]
  (nth sigfun 3))

(defn create-sigfun
  "Create a sigfun object."
  [name priority arity function]
  '(name priority arity function))




(defn tick-received
  []
  (println "Tick!"))

(defn loop-init 
  ;; Init: stuff to do before starting the loop
  ;; To be called once every reset
  [] 
  ())

(def embla-signals
  "embla-signals is a hash, constituted by signame in key, and a list of sigfun.
  Each sigfun has form (name priority arity function)."
  {
   :tick [#(tick-received)]
   :stop [#(System/exit 0)]
   :reset [#(loop-init)]
   }
  )

(defn add-signal
  ""
  ([signame] (update-signal signame))
  ([signame & sigfun] (update-signal signame sigfun)))

(defn update-signal
  ;; Updates, or creates a new signal if no match. Allow to add custom signals.
  ;; Create the association signame and functions if any.
  ;; Sigfun HAS TO BE a list of functions.
  ([signame]
   (alter-var-root embla-signals (fn [var]
                                   (assoc var signame nil))))
  ([signame & sigfun]
   (alter-var-root embla-signals (fn [var]
                                   (assoc var signame sigfun)))))

(defn delete-signal
  ""
  [signame]
  (alter-var-root embla-signals 
                      #(dissoc % signame)))

(defn subscribe 
  ;; Subscription mechanism.
  ;; Subscribe to a signal (either user-defined or core)
  ;; and supply a function that will treat this signal, to be called
  ;; every time the signal is emitted.
  ;; TODO: Some kind of arity check?
  [signame sigfun]
  (if (nil? (get embla-signals signame))
    (println "No signal matching." signame)
    (do 
      (alter-var-root embla-signals 
                      #(assoc % signame (cons sigfun (get % signame))))
      (println "Matching signal.") signame)))
  
(defn unsubscribe
  ""
  [signame & sigfuns]
  (let [is-present 
        (fn loop [sig lsigs]
          (if (empty? lsigs)
            false
            (let [first-sig (first lsigs)]
              [name-fun (sigfun-name sig)]
              (if (= (name-fun (sigfun-name first-sig)))
                true
                (loop sig (rest lsigs))))))]
    (let [drop-fun 
          (fn drop [sigf]
            (if (empty? sigf)
              '()
              (if (is-present (first sigf) sigfuns)
                (cons (drop (rest sigf)))
                (cons (first sigf) (drop (rest sigf))))))]
      (if (nil? (get embla-signals signame))
        (println "No signal matching." signame)
        (do
          (alter-var-root embla-signals
                          #(assoc % signame (drop-fun (get % signame)))))))))
  
(defn execute [fun args]
  (apply fun args))

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
          (doseq [f funlist] (f)))))))

(defn start-repl
  []
  ())

(defn gameloop 
  ;; The gameloop. Set up stuff if first launch, else keep sending 'tick' until
  ;; close time.
  ;; TODO: How the fuck do we shut the loop?
  [] 
  (loop-init)
  (let [time-last-tick (atom 0)]
    (while true
      (let [current (System/currentTimeMillis)]
        (let [delta-t (- current @time-last-tick)]
          (if (> delta-t 1000)
            (do
              (emit :tick)
              (swap! time-last-tick (fn [x] current)))))))))

(defn -main
  "Launch Embla. REPL live coding optional"
  [& args]
  (let [repl-needed false]  
    (println "Hello, Embla!")
    (println "Beginning gameloop...")*
    
    ;; REPL?
    (if (true? repl-needed)
      (start-repl))
    ;; Loop start
    (gameloop)))

