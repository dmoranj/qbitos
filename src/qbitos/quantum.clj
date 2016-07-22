(ns qbitos.quantum
  (:gen-class))

(use 'qbitos.complex)
(use 'qbitos.classical)

(def MARGIN 0.000001)

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


(defn get-subvector-ocurrences[bits begin vectors subvector]
  (let [ belong-to (fn [single-vec]
                     (= (.substring single-vec begin (+ begin (.length subvector))) subvector)) ]

    {:subvector subvector :vectors (filter belong-to vectors)} ))


(defn vectors-to-indexes[subvector]
  ;; Add it to the vectors field

  (assoc
    subvector
    :vectors
    (map #(bit-sequence-value (bit-values %)) (:vectors subvector))))


(defn get-probability[state subvector]
  (let [ amplitudes (map #(.get state %) (:vectors subvector)) ]
  (assoc
    subvector
    :probability
    (reduce + (map #(Math/pow (.abs %) 2) amplitudes))
    )))


(defn general-accumulate[accumulated-vector item]
  (let [last-result (if (empty? accumulated-vector)
                      0
                      (:accumulate (last accumulated-vector)))
        new-result (+ last-result (:probability item))]
    (conj accumulated-vector (assoc item :accumulate new-result) )))


(defn select-from-probabilities[probabilities]
  (let [ filtered (filter #(> (:probability %) MARGIN) probabilities)
         sorted (sort-by :probability probabilities)
         accumulated (reduce general-accumulate [] sorted)
         random-value (Math/random)
         selected (last (take-while #(< (:accumulate %) random-value) accumulated))
         ]
      (if selected
        selected
        (first accumulated))))

(defn gmeasure[state begin end]
  (let [ vector-bits (log2 (.length state))
         subvector-bits (- end begin)
         subvectors (map #(generate-bits % subvector-bits) (range (Math/pow 2 subvector-bits)))
         vectors (map #(generate-bits % vector-bits) (range (Math/pow 2 vector-bits)))
         subvector-occurrences (map (partial get-subvector-ocurrences vector-bits begin vectors) subvectors)
         subvector-indexes (map vectors-to-indexes subvector-occurrences)
         probabilities (map (partial get-probability state) subvector-indexes)
         selected (select-from-probabilities probabilities)
         filtered (get-subvector-ocurrences vector-bits begin vectors (:subvector selected))
         amplitudes (map #(.get state (bit-sequence-value (bit-values %))) (:vectors filtered))
         total (reduce + 0 (map #(Math/pow (.abs %) 2) amplitudes))
         normalized (map #(.div % (Math/sqrt total)) amplitudes)
         indexed (map #(bit-sequence-value (bit-values %)) (:vectors filtered))
         empty-vector (vec (take (.length state) (repeat [[0 0]])))
         indexed-amplitudes (reduce #(assoc %1 (first %2) (second %2)) {} (partition 2 (interleave indexed normalized)))
         ]

    (vec (map-indexed #(if (= (get indexed-amplitudes %1) nil)
                    %2
                    [(complex-to-array (get indexed-amplitudes %1))]
                    ) empty-vector))))


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
        responses (map #(-> (f bits %) (mod max-value) int) values)
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
    (apply msum (pmap (partial define-term bits) terms))))
