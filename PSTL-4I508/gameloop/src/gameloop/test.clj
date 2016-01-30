(defn myloop
  (loop [continue 0]
    (if (= continue 0)
      (do
        (println "Continue!")
        (recur)
        )
      )
    )
  )
