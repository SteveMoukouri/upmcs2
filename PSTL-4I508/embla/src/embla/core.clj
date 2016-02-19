(ns embla.core
  (:require [embla.emblagl :as egl]
            [embla.signals :as sig])
  (:gen-class))

(defn -main
  "Launch Embla. REPL live coding optional"
  [& args]
  (println "Embla starting...")
        
  (try
    (egl/versions)
    (egl/opengl-init)
    (egl/opengl-loop)
    (finally
      (egl/opengl-terminate))))
