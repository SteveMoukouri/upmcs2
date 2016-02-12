(ns embla.core
  (:require [embla.emblagl :as egl])
  (:gen-class))

(defn -main
  "Launch Embla. REPL live coding optional"
  [& args]
  (let [repl-needed false]  
    (println "Embla starting...")
        
    ;; REPL?
    (if (true? repl-needed)
      (start-repl))
    ;; Loop start
    (try
      (egl/versions)
      (egl/opengl-init)
      (egl/opengl-loop)
      (finally
        (egl/opengl-terminate)))))

