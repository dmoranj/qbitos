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
  (def f3 X1))
