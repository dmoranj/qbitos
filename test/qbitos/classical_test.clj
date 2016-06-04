(ns qbitos.classical-test
  (:require [clojure.test :refer :all]
            [qbitos.complex :refer :all]
            [qbitos.classical :refer :all]))

(defbits |10>)
(defbits |01>)
(defbits |11>)
(defbits |00>)
(defbits |010>)
(defbits |110>)
(defbits <10|)

(defoperator X1 2)
(defoperator Y0 3)

(deftest multibit-definition
  (testing "Definition of multibit constants."
    (is (= (to-persistent |10>) [[[0 0]] [[0 0]] [[1 0]] [[0 0]]]))))

(deftest multibit-bravector
  (testing "Define multibit bra vectors."
    (is (= <10| [[[0 0] [0 0] [1 0] [0 0]]]))))

(deftest conditional-not-constant
  (testing "Test the 2-bit constant CNOT"
    (is (= (mmul C01 |10>) |11>))
    (is (= (mmul C01 |00>) |00>))
    (is (= (mmul C01 |01>) |01>))
    ))

(deftest number-operator
  (testing "Number operator conditions."
    (is (= (mmul n n) n))
    (is (= (mmul ñ ñ) ñ))
    (is (= (mmul ñ n) (mmul n ñ)))
    (is (= (mmul ñ n) [[[0 0] [0 0]] [[0 0] [0 0]]]))
    (is (= (msum n ñ) (ident 2)))))

(deftest operator-combination
  (testing "Operator combination macro"
    (is (= (mmul X1 |10>) |11>))
    (is (= (mmul X1 |01>) |00>))
    (is (= (mmul X1 |10>) |11>))
    (is (= (mmul Y0 |010>) [[[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 -1]] [[0 0]]]))
    ))

;; (deftest generic-conditional-not
;;  (testing "Creation of generic conditional NOT doors"
;;    (defcij 1 0 2)
;;    (is (= (to-persistent C-10) C10))
;;    ))
