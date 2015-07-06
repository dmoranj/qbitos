(ns qbitos.scenarios
  (:gen-class))

(use 'qbitos.classical)
(use 'qbitos.complex)

(defn loadDeutch []
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
        leadingZeros (take (- (dec bits) (count binaryExpansion)) (repeat \0))
        indexes (range bits)
        bitList (partition 2 (interleave indexes (concat leadingZeros binaryExpansion)))]
    (apply mmul (map #(apply createCij [(first %) (dec bits) bits]) (filter #(= (second %) \1) bitList)))
    ))

(defn loadBernsteinVazirani [a bits]
  (def fx (createFa a bits)))
