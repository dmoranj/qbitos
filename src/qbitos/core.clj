(ns qbitos.core
  (:gen-class))

(defn qbit [x]
  (trans (matrix x)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(defn sum [x y]
  (+ x y))


(sum 7 4)
