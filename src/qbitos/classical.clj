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
  (let [pruebas (-> x str seq first)
        bits (-> x str seq rest butlast vec)]
    (case pruebas
      \| `(def ~x (apply tensorp (map #(if (= (str %) "0") |0> |1>) ~bits)))
      \< `(def ~x (trans (apply tensorp (map #(if (= (str %) "0") |0> |1>) ~bits)))))))


(defmacro defoperator[x n]
  (let [operators (vec (map symbol (.split (str x) "\\d+")))
        indices (vec (map #(Integer/parseInt %) (rest (.split (str x) "\\D+"))))
        operatorList (partition 2 (interleave operators indices))
        initial (vec (repeat n (ident 2)))
        addOperator (fn [list [op index]] (assoc list index op))
        matrix (reduce addOperator initial operatorList)
        ]
    `(def ~x (apply tensorp ~matrix))
    ))

(defoperator X0X2 4)

