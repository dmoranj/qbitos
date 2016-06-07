(ns qbitos.visualization
  (:gen-class))

(use 'qbitos.complex)
(use 'qbitos.classical)

(use 'clojure.pprint)

(def visual-precision 0.001)


(defn print-complex[n]
  (let [trunc-real (clojure.pprint/cl-format nil "~,2f" (get-real n))
        trunc-img (clojure.pprint/cl-format nil "~,2f" (get-imaginary n))
         ]
    (str "(" trunc-real "+" trunc-img "i) ")
    )
  )

(defn print-bra[n]
  (let [base-list (range (count n))
        bit-vectors (map #(generate-bits % (log2 (count n))) base-list)
        coefficients (map #(-> n  (nth %) (nth 0) complex-from-array) base-list)
        is-significant (fn [[i bits coefficient]] (> (.abs coefficient) visual-precision))
        significant-terms (filter is-significant (partition 3 (interleave base-list bit-vectors coefficients)))
        prepare-term (fn [[i bits coefficient]]
                       (if (= 1.0 (get-real coefficient))
                         (str "|" bits ">")
                         (str (print-complex coefficient) "|" bits ">")
                         ))

        ]
    (reduce str (interpose " + " (map prepare-term significant-terms)))
  ))

(defn print-operator[n]
  (str "[" (reduce #(str %1 %2 "\n") "\n" (doall (map #(reduce str (map print-complex %)) n))) "]\n")
  )

(defn print-matrix[n]
  (let [representation (to-persistent n
                                      )]
    (if (= (count (representation 0)) 1)
      (print-bra representation)
      (print-operator representation)
      )
    )
  )

(defn visualize [n]
  (println (case (.getName (class n))
    "org.jblas.ComplexDoubleMatrix" (print-matrix n)
    "org.jblas.ComplexDouble" (print-complex n)
    n
    ) "\n")
  )

