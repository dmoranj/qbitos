(ns qbitos.classical
  (:gen-class))

(use 'qbitos.classical)

(defn log2[x]
  (int (/ (Math/log x) (Math/log 2)))
  )

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
  (let [probability-vector (reduce accumulate [ ] (sort-by second (get-probability-vector state)))
        random-value (Math/random)
        ]
    (first (last (take-while #(< (second %) random-value) probability-vector)))))


