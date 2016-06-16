(ns qbitos.scenarios
  (:gen-class))

(use 'qbitos.classical)
(use 'qbitos.complex)

(defoperator X1 2)
(defoperator H0H1 2)
(defoperator X0X1 2)
(defoperator H0 2)


(defn loadDeutch []

 (def f0 (ident 4))
 (def f1 C01)
 (def f2 (mmul C01 X1))
 (def f3 X1)
 )

(defn createFa [a bits]
  (let [binaryExpansion (Integer/toBinaryString a)
        leadingZeros (take (- (dec bits) (count binaryExpansion)) (repeat \0))
        indexes (range bits)
        bitList (partition 2 (interleave indexes (concat leadingZeros binaryExpansion)))]
    (apply mmul (map #(apply createCij [(first %) (dec bits) bits]) (filter #(= (second %) \1) bitList)))
    ))

(defn loadBernsteinVazirani [a bits]
  (def BVx (createFa a bits)))

(defn createFModuloA[a offset]
  (fn [bits x]
    (let [abit (bit-values (generate-bits a bits))
          xvalue (mod (+ offset x) (Math/pow 2 bits))
          xbit (bit-values (generate-bits xvalue bits))]
      (map #(bit-xor (first %) (second %)) (partition 2 (interleave abit xbit))))))

(defn loadSimonProblem[a bits]
  (def fSimon (createFModuloA a))
  )
