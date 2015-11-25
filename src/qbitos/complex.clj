(ns qbitos.complex
  (:gen-class))

;; JBLAS Conversion Routines
;;----------------------------------------------------------------------------------------------
(defn complex-from-array[c]
  (new org.jblas.ComplexDouble (first c) (second c)))

(defn complex-to-array[c]
  [(.real c) (.imag c)])

(defn is-jblas-double[c]
  (= (type c) org.jblas.ComplexDouble))

(defn is-jblas-complex-matrix[m]
  (= (type m) org.jblas.ComplexDoubleMatrix))

(defn is-vector[c]
  (and (= (type c) clojure.lang.PersistentVector) (number? (first c))))

(defn is-matrix[m]
  (and (= (type m) clojure.lang.PersistentVector) (= (type (first m)) clojure.lang.PersistentVector) ))

(defn complex [a b]
  (new org.jblas.ComplexDouble (double a) (double b)))

(defn coerce-jblas-double[a]
  (if (is-jblas-double a)
    a
    (if (is-vector a)
      (complex (first a) (second a))
      nil
      )))

(defn get-real[c]
  (-> c coerce-jblas-double .real))

(defn get-imaginary[c]
  (-> c coerce-jblas-double .imag))

(defn from-persistent[m]
  (let [real-matrix (new org.jblas.DoubleMatrix (into-array (map #(into-array Double/TYPE (map get-real %)) m)))
        imaginary-matrix (new org.jblas.DoubleMatrix (into-array (map #(into-array Double/TYPE (map get-imaginary %)) m)))]
    (new org.jblas.ComplexDoubleMatrix real-matrix imaginary-matrix)))

(defn to-persistent[m]
  (vec (map #(vec (map complex-to-array %)) (.toArray2 (from-persistent m)))))

(defn coerce-jblas-matrix[a]
  (if (is-jblas-complex-matrix a)
    a
    (if (is-matrix a)
      (from-persistent a)
      nil
      )))


(defn rowToDouble[row]
  (vec (map #(vec (map double %)) row)))

(defn matrixToDouble[matrix]
  (vec (map rowToDouble matrix)))


;; Complex Algebra functions
;;----------------------------------------------------------------------------------------------
(defn conjugate[a]
  {:pre [(or (is-jblas-double a) (is-vector a))]
   :post [(is-jblas-double %)]}
  (let [c (coerce-jblas-double a)]
    (.conj c)))

(defn mul[a b]
  {:pre [(and (or (is-jblas-double a) (is-vector a)) (or (is-jblas-double b) (is-vector b)))]
   :post [(is-jblas-double %)]}
  (let [ca (coerce-jblas-double a)
        cb (coerce-jblas-double b)]
    (.mul ca cb)))

(defn sum[a b]
  {:pre [(and (or (is-jblas-double a) (is-vector a)) (or (is-jblas-double b) (is-vector b)))]
   :post [(is-jblas-double %)]}
    (let [ca (coerce-jblas-double a)
          cb (coerce-jblas-double b)]
    (.add ca cb)))

(defn single-mmul[a b]
  {:pre [(and (or (is-jblas-complex-matrix a) (is-matrix a)) (or (is-jblas-complex-matrix b) (is-matrix b)))]
   :post [(is-jblas-complex-matrix %)]}
  (let [ca (coerce-jblas-matrix a)
          cb (coerce-jblas-matrix b)]
    (.mul ca cb)))

(defn mmul[& matrixList]
  (reduce #(single-mmul %1 %2) (first matrixList) (rest matrixList)))

(defn single-msum[a b]
  {:pre [(and (or (is-jblas-complex-matrix a) (is-matrix a)) (or (is-jblas-complex-matrix b) (is-matrix b)))]
   :post [(is-jblas-complex-matrix %)]}
  (let [ca (coerce-jblas-matrix a)
          cb (coerce-jblas-matrix b)]
    (.add ca cb)))

(defn msum[& matrixList]
  (reduce #(single-msum %1 %2) (first matrixList) (rest matrixList)))

(defn trans[a]
  {:pre [(or (is-jblas-complex-matrix a) (is-matrix a))]
   :post [(is-jblas-complex-matrix %)]}
  (let [c (coerce-jblas-matrix a)]
    (.transpose c)))

(defn null[n]
  {:pre [(number? n)]
   }
  (new org.jblas.ComplexDoubleMatrix n n))

(defn ident[n]
  (from-persistent
    (vec (map vec (partition n
      (for [x (range n)
            y (range n)]
        (if (= x y)
          [1 0]
          [0 0])))))))

(defn inv[n]
  (from-persistent
    (vec (map vec (partition n
      (for [x (range n)
            y (range n)]
        (if (= (+ x y) (dec n))
          [1 0]
          [0 0])))))))

(defn format-comp [x]
  (str (first x) "+" (second x) "i"))

(defn format-mat[m]
  (partition (count m)
  (for [x (range (count m))
        y (range (count m))]
    (format-comp (get-in m [x y])))))

(defn tensorp-indices
  [n r]
  (let [names (vec (map #(symbol (str "x" %)) (range n)))
        parameters (vec (reduce #(concat %1 [%2 `(range ~r)]) [] names))]
  (eval `(for ~parameters ~names))))

(defn cmul[c a]
  {:pre [(and (or (is-jblas-double c) (is-vector c)) (or (is-jblas-complex-matrix a) (is-matrix a)))]
   :post [(is-jblas-complex-matrix %)]}
  (let [ca (coerce-jblas-matrix a)
        cc (coerce-jblas-double c)]
    (.mmul ca cc)))

(defn get-matrix-element[m i j]
  (.get m i j))

(defn tensorp [& matObjects]
  (let [n (count matObjects)
        matrices (map coerce-jblas-matrix matObjects)
        rows (.rows (first matrices))
        columns (.columns (first matrices))
        row-indexes (vec (tensorp-indices n rows))
        column-indexes (vec (tensorp-indices n columns))]
    (trans (from-persistent
            (partition (int (Math/pow rows n))
      (for [x (range (count row-indexes))
            y (range (count column-indexes))
            :let [element-indexes (vec (partition 2 (interleave (get row-indexes x) (get column-indexes y))))
                  element-values (map-indexed #(apply get-matrix-element (nth matrices %1) (into-array Integer/TYPE %2)) element-indexes)]]
        (complex-to-array (reduce mul [1 0] element-values))

        )))
    )
    ))


