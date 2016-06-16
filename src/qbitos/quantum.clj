(ns qbitos.quantum
  (:gen-class))

(use 'qbitos.complex)
(use 'qbitos.classical)

(defn get-probability[[[real imaginary]]]
  (+ (* real real) (* imaginary imaginary))
  )


(defn get-probability-vector[state]
  (let [stateNumber (count state)
        bits (log2 stateNumber)]
    (for [x (range stateNumber)
          :let [ name (generate-bits x bits)
                 probability (get-probability (state x))
                ]]
      (vector name probability))))


(defn accumulate[accumulated-vector item]
  (let [last-result (if (empty? accumulated-vector)
                      0
                      (last (last accumulated-vector)))]
    (conj accumulated-vector (vector (first item) (+ last-result (second item) )))))

(defn measure[state]
  (let [probability-vector (reduce accumulate [ ] (sort-by second (get-probability-vector (to-persistent state))))
        random-value (* (Math/random) (-> probability-vector last last))
        ]
    (str "|" (first (last (take-while #(< (second %) random-value) probability-vector))) ">")))


(defn define-term[bits term]
  (let [selector (create-operator (first term) (* 2 bits))
        value (create-operator (second term) (* 2 bits))
        ]
    (mmul selector value)))

(defn caseTable[f bits]
  (let [max-value (Math/pow 2 bits)
        values (range max-value)
        total-bits (range (* 2 bits))
        bit-inputs (map #(generate-bits % bits) values)
        responses (map #(-> % f (mod max-value) int) values)
        bit-outputs (map #(generate-bits % bits) responses)
        identity-output  (apply str (map #(str "I" %) total-bits))
        get-n-operator (fn [x]
                         (apply str (map-indexed
                           (fn [idx itm]
                             (if (= itm \1)
                                (str "n" idx)
                                (str "ñ" idx)
                                )) x)))
        get-x-operator (fn [x]
                         (apply str (map-indexed
                           (fn [idx itm]
                             (if (= itm \1)
                                (str "X" (+ bits idx))
                                ""
                                )) x)))

        clean-empty (fn [x]
                      (if (= x "")
                        identity-output
                        x))
        n-operators (map get-n-operator bit-inputs)
        x-operators (map clean-empty (map get-x-operator bit-outputs))
        terms (partition 2 (interleave n-operators x-operators))
        ]
    (apply msum (map (partial define-term bits) terms))))
