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

(defn mmul[a b]
  )

(defn msum[a b]
  )

(defn trans[a]
  )

(defn null[n]
  (vec(take n (repeat (vec (repeat n (complex 0 0)))))))

(defn ident[n]
  (vec (partition n
    (for [x (range n)
          y (range n)]
      (if (= x y)
        (complex 1 0)
        (complex 0 0))))))


(defn format-comp [x]
  (str (first x) "+" (second x) "i"))

(defn format-mat[m]
  (partition (count m)
  (for [x (range (count m))
        y (range (count m))]
    (format-comp (get-in m [x y])))))


;;-----------------------------------------

(def A (complex 5 3))
(def B (complex 2 -9))

(format-comp A)

(sum A B)
(mul A A)
(mul A (conjugate A))


(ident 3)

(format-mat (null 3))
