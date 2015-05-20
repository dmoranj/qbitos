(ns qbitos.complex
  (:gen-class))

(defn complex [a b]
  [a b])

(defn conjugate[a]
  [ (first a) (- (second a))])

(defn mul[a b]
  [(- (* (first a) (first b)) (* (second a) (second b)))
   (+ (* (second a) (first b)) (* (first a) (second b)))])

(defn sum[a b]
  [(+ (first a) (first b)) (+ (second a) (second b))])

(defn mij[x y a b]
  (let [rowi (get a x)
        columnj (map #(get % y) b)]
    (reduce #(sum %1 %2) [0 0] (map #(mul (first %) (second %)) (partition 2 (interleave rowi columnj))))))

(defn mmul[a b]
  (let [rows (count a)
        columns (count (first b))]
    (partition columns
      (for [x (range rows) y (range columns)]
        (mij x y a b)))))

(defn transform[n f]
  (vec (map vec (partition n
    (for [x (range n)
          y (range n)]
      (f x y))))))

(defn msum[a b]
  (transform (count a) #(sum (get-in a [%1 %2]) (get-in b [%1 %2]))))

(defn trans[a]
  (transform (count a) #(get-in a [%2 %1])))

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
    (partition (int (Math/pow rows 2))
      (for [x (range (count row-indexes))
            y (range (count column-indexes))
            :let [element-indexes (vec (partition 2 (interleave (get row-indexes x) (get column-indexes y))))
                  element-values (map-indexed #(get-in (nth matrices %1) %2) element-indexes)]]
        (reduce mul [1 0] element-values)))))

