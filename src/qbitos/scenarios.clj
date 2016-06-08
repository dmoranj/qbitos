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


(defn create-random-operator[n]
    {:pre [(= (mod n 2) 0)] }
  (let [operators ["I", "X", "Y", "Z", "H"],
        operator-str (reduce str (for [x (range n)
          :let [operator (nth operators (* (Math/random) (count operators)))]]
      (str operator x)))]
    {
     :string operator-str
     :matrix (create-operator operator-str n)
     }))

(defn get-operator-output [m]
  (let [n (/ (count m) 2)
        inputs (for [x (range (Math/pow 2 n))
                     :let [binaryExpansion (Integer/toBinaryString x)
                           leadingZeros (take (- n (count binaryExpansion)) (repeat \0))
                           output-register (take n (repeat \0))]]
                 (vec (map #(vector (vector (-> % str Integer/parseInt) 0)) (concat leadingZeros binaryExpansion output-register))))]
    (pmap #(vector % (mmul m %)) inputs)))

(defn evaluation-criteria [outputs]
  )


(defn next-generation [pool]
  (let [values (pmap #(hash-map :operator % :results (get-operator-output (:matrix %))) pool)
        ]
    values
    )


  ; evaluate fitness function for every matrix, based in its outputs
  ; create the next generation of matrices based on the fitness results of the previous
  )

(defn create-binary-periodic [a bits pool-size]
  (let [pool (take pool-size (repeatedly (partial create-random-operator bits)))
        ]

    (next-generation pool)))

  ; repeate the process until at least a matrix is found complying with the evaluation criteria

(def results (create-binary-periodic 5 6 1))

;(count (:results (first results)))

;(defn loadSimonsProblem [a bits]
;  (def SPx (create-binary-periodic a bits)))


