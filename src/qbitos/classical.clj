(ns qbitos.classical
  (:gen-class))

(use 'qbitos.complex)

(def |0> (from-persistent [[[1 0]][[0 0]]]))
(def |1> (from-persistent [[[0 0]][[1 0]]]))
(def <0| (from-persistent [[[1 0][0 0]]]))
(def <1| (from-persistent [[[0 0][1 0]]]))

(def C01 (from-persistent [[[1 0 ][0 0][0 0 ][0 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          [[0 0 ][0 0][0 0 ][1 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          ]))

(def C10 (from-persistent [[[1 0 ][0 0][0 0 ][0 0]]
          [[0 0 ][0 0][0 0 ][1 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          ]))

(def S10 (from-persistent [[[1 0 ][0 0][0 0 ][0 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          [[0 0 ][0 0][0 0 ][1 0]]
          ]))

(def S01 S10)

(def S00 (from-persistent [[[0 0 ][0 0][0 0 ][1 0]]
          [[0 0 ][1 0][0 0 ][0 0]]
          [[0 0 ][0 0][1 0 ][0 0]]
          [[1 0 ][0 0][0 0 ][0 0]]
          ]))

(def S11 S00)

(def I (from-persistent [[[1 0][0 0]]
        [[0 0][1 0]]]))

(def X (from-persistent [[[0 0][1 0]]
        [[1 0][0 0]]]))
(def Y (from-persistent [[[0 0][0 -1]]
        [[0 1][0 0]]]))
(def Z (from-persistent [[[1 0][0 0]]
        [[0 0][-1 0]]]))

(def H (cmul (complex (/ 1 (Math/sqrt 2)) 0) [[[1 0][1 0]][[1 0][-1 0]]]))

(def n (from-persistent [[[0 0 ][0 0]]
        [[0 0 ][1 0]]]))

(def ñ (from-persistent [[[1 0][0 0]]
         [[0 0][0 0]]]))

(defmacro defbits[x]
  (let [pruebas (-> x str seq first)
        bits (-> x str seq rest butlast vec)]
    (case pruebas
      \| `(def ~x (apply tensorp (map #(if (= (str %) "0") |0> |1>) ~bits)))
      \< `(def ~x (trans (apply tensorp (map #(if (= (str %) "0") |0> |1>) ~bits)))))))

(defn create-operator [x n]
  (let [operators (vec (map #(eval (symbol %)) (.split (str x) "\\d+")))
        indices (vec (map #(Integer/parseInt %) (rest (.split (str x) "\\D+"))))
        operatorList (partition 2 (interleave operators indices))
        initial (vec (repeat n (ident 2)))
        addOperator (fn [list [op index]] (assoc list index op))
        matrix (reduce addOperator initial operatorList)]
    (apply tensorp matrix)))

(defmacro defoperator[x n]
  (let [operator (create-operator (str x) n)]
    `(def ~x ~operator)
    ))

(defn createCij[i j n]
  (let [opX (create-operator (str "X" j) n)
        opZ (create-operator (str "Z" i) n)
        unit (ident (int (Math/pow 2 n)))]
     (msum
        (->> opX (msum unit) (cmul [0.5 0]))
        (->> opX (cmul [-1 0]) (msum unit) (mmul opZ) (cmul [0.5 0])))))

(defmacro defcij[i j n]
  (let [operator-name (symbol (str "C-" i j))
        operator (createCij i j n)]
    `(def ~operator-name ~operator)))

(defn generate-bits [n bits]
  (let [binary-expansion (Integer/toBinaryString n)
        leading-zeroes (- bits (count binary-expansion))]
    (str (reduce str (take leading-zeroes (repeat "0"))) binary-expansion)))

(defmacro generate-vectors [bits]
  (let [define-bras (for [x (range (Math/pow 2 bits))
          :let [ bra (symbol (str "|" (generate-bits x bits) ">"))]]
              `(defbits ~bra))
        define-kets (for [x (range (Math/pow 2 bits))
          :let [ ket (symbol (str "<" (generate-bits x bits) "|"))]]
              `(defbits ~ket))]
    `(do ~@define-bras ~@define-kets)))


(defn bit-values[bit-string]
  (map #(- (int %) 48) (seq bit-string)))

(defn bit-sequence-value [s]
  (int (reduce + (map-indexed #(* %2 (Math/pow 2 (- (dec (count s)) %1))) s))))
