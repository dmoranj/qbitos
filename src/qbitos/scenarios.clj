(ns qbitos.scenarios
  (:gen-class))

(use 'qbitos.classical)
(use 'qbitos.complex)

(defn loadDeutch []
  (defbits |00>)
  (defbits |01>)
  (defbits |10>)
  (defbits |11>)
  (defoperator X1 2)

  (def f0 (ident 4))
  (def f1 C01)
  (def f2 (mmul C01 X1))
  (def f3 X1)

  (defoperator H0H1 2)
  (defoperator X0X1 2)
  (defoperator H0 2)
  )


(defn createFa [a bits]
  (let [binaryExpansion (Integer/toBinaryString a)
        indexes (range (count binaryExpansion))]
    (create-operator (reduce #(if (= (second %2) \1)
               (str %1 (str "X" (first %2)))
               %1
               ) "" (zipmap indexes binaryExpansion))
    bits))
  )


(defn loadBernsteinVazirani [a bits]
  (def fx (createFa a))
  )
