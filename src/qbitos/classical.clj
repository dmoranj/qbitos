(ns qbitos.classical
  (:gen-class))

(use 'qbitos.complex)

(def |0> [[(complex 1 0)] [(complex 0 0)]])
(def |1> [[(complex 0 0)] [(complex 1 0)]])
(def C10 [[[1 0 ][0 0][0 0 ][0 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          [[0 0 ][0 0][0 0 ][1 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          ])
(def C01 [[[1 0 ][0 0][0 0 ][0 0]]
          [[0 0 ][0 0][0 0 ][1 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          ])
(def S10 [[[1 0 ][0 0][0 0 ][0 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          [[0 0 ][0 0][0 0 ][1 0]]
          ])
(def S01 S10)
(def S00 [[[0 0 ][0 0][0 0 ][1 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          [[1 0 ][0 0][0 0 ][0 0]]
          ])
(def S11 S00)

(def X [[[0 0][1 0]]
        [[1 0][0 0]]])
(def Y [[[0 0][0 -1]]
        [[0 1][0 0]]])
(def Z [[[1 0][0 0]]
        [[0 0][-1 0]]])

(def H (cmul [(/ 1 (Math/sqrt 2)) 0] [[[1 0][1 0]][[1 0][-1 0]]]))

(def n [[[0 0 ][0 0]]
        [[0 0 ][1 0]]])

(def ñ [[[1 0][0 0]]
         [[0 0][0 0]]])

(defmacro defbits[x]
  (let [bits (-> x str seq rest butlast vec)]
      `(def ~x (apply tensorp (map #(if (= (str %) "0") |0> |1>) ~bits)))))

(defmacro defoperator[x n]
  (let [operation (-> x str seq first str symbol)
        indexes (Integer/parseInt (-> x str seq rest first str))
        operators (vec (repeat n (ident 2)))]
      `(def ~x (apply tensorp (assoc ~operators ~indexes ~operation)))
    ))
