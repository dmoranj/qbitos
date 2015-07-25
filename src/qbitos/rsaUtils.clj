(ns qbitos.rsaUtils
  (:gen-class))


(use 'clojure.set)
(use 'clojure.math.numeric-tower)


(defn divisors [n]
  (set (filter #(= 0 (mod n %)) (range 1 (inc n)))))

(defn prime [n]
  (= 1 (count (divisors n))))

(defn Gn [n]
  (conj (set (filter #(= 1 (count (intersection (divisors n) (divisors %)))) (range 1 n))) 1))

(defn getPowers
  ([n N]   (cons n (getPowers (mod (* n n) N) n N)))
  ([n base N] (cons n (lazy-seq (getPowers (mod (* n base) N) base N))))
  )

(defn GnSub [n N]
  {:pre [((Gn N) n)]
   }
  (conj (set (take-while #(not= % 1) (getPowers n N))) 1))

(defn getOrder [n N]
  {:pre [((Gn N) n)]
   }
  (count (GnSub n N)))

(defn mulInverse [n N]
  {:pre [((Gn N) (mod n N))]}
  (bigint (first (filter #(= 1N (mod (* n %) N)) (Gn N)))))
