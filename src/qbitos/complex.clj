(ns qbitos.complex
  (:gen-class))

;; JBLAS Conversion Routines
;;----------------------------------------------------------------------------------------------
(defn complex-from-array[c]
  (new org.jblas.ComplexDouble (first c) (second c)))

(defn complex-to-array[c]
  [(.real c) (.imag c)])

(defn get-real[c]
  (-> c first double))

(defn get-imaginary[c]
  (-> c second double))

(defn from-persistent[m]
  (let [real-matrix (new org.jblas.DoubleMatrix (into-array (map #(into-array Double/TYPE (map get-real %)) m)))
        imaginary-matrix (new org.jblas.DoubleMatrix (into-array (map #(into-array Double/TYPE (map get-imaginary %)) m)))]
    (new org.jblas.ComplexDoubleMatrix real-matrix imaginary-matrix)))

(defn to-persistent[m]
  (vec (map #(vec (map complex-to-array %)) (.toArray2 (from-persistent m)))))

(defn rowToDouble[row]
  (vec (map #(vec (map double %)) row)))

(defn matrixToDouble[matrix]
  (vec (map rowToDouble matrix)))

(defn is-jblas-double[c]
  (= (type c) org.jblas.ComplexDouble))

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

(defn mij[x y a b]
  (let [rowi (get a x)
        columnj (map #(get % y) b)]
    (reduce #(sum %1 %2) [0 0] (map #(mul (first %) (second %)) (partition 2 (interleave rowi columnj))))))

(defn single-mmul[a b]
  {:pre [(= (count (first a)) (count b))]
   :post [(and (= (count %) (count a)) (= (count (first %)) (count (first b))))]}
  (let [rows (count a)
        columns (count (first b))]
    (vec (map vec (partition columns
      (for [x (range rows) y (range columns)]
        (mij x y a b)))))))

(defn mmul[& matrixList]
  (reduce #(single-mmul %1 %2) (first matrixList) (rest matrixList)))

(defn transform[na nb f]
  (vec (map vec (partition na
    (for [x (range na)
          y (range nb)]
      (f x y))))))

(defn single-msum[a b]
  {:pre [(and (= (count a) (count b)) (= (count (first a)) (count (first b))))]}
  (transform (count a) (count (first a)) #(sum (get-in a [%1 %2]) (get-in b [%1 %2]))))

(defn msum[& matrixList]
  (reduce #(single-msum %1 %2) (first matrixList) (rest matrixList)))

(defn trans[a]
  {:post [(and (= (count %) (count (first a))) (= (count (first %)) (count a)))]
   }
  (let [rows (count a)
        columns (count (first a))]
    (vec (map vec (partition rows
    (for [x (range columns)
          y (range rows)]
      (get-in a [y x])))))))

(defn null[n]
  (vec(take n (repeat (vec (repeat n (complex 0 0)))))))

(defn ident[n]
  (vec (map vec (partition n
    (for [x (range n)
          y (range n)]
      (if (= x y)
        (complex 1 0)
        (complex 0 0)))))))

(defn inv[n]
  (vec (map vec (partition n
    (for [x (range n)
          y (range n)]
      (if (= (+ x y) (dec n))
        (complex 1 0)
        (complex 0 0)))))))

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

(defn tensorp [& matrices]
  (let [n (count matrices)
        rows (count (first matrices))
        columns (count (first (first matrices)))
        row-indexes (vec (tensorp-indices n rows))
        column-indexes (vec (tensorp-indices n columns))]
    (trans (vec (map vec (partition (int (Math/pow rows n))
      (for [x (range (count row-indexes))
            y (range (count column-indexes))
            :let [element-indexes (vec (partition 2 (interleave (get row-indexes x) (get column-indexes y))))
                  element-values (map-indexed #(get-in (nth matrices %1) %2) element-indexes)]]
        (reduce mul [1 0] element-values))))))))

(defn cmul[c a]
  (let [mrow (fn [row] (vec (map #(mul c %) row)))]
    (vec (map mrow a))))

