(ns qbitos.classical-test
  (:require [clojure.test :refer :all]
            [qbitos.complex :refer :all]
            [qbitos.classical :refer :all]))

(defbits |10>)

(deftest multibit-definition
  (testing "Definition of multibit constants."
    (is (= |10> [[[0 0]] [[0 0]] [[1 0]] [[0 0]]]))))

(deftest number-operator
  (testing "Number operator conditions."
    (is (= (mmul n n) n))
    (is (= (mmul nn nn) nn))
    (is (= (mmul nn n) (mmul n nn)))
    (is (= (mmul nn n) [[[0 0] [0 0]] [[0 0] [0 0]]]))
    (is (= (msum n nn) (ident 2)))
    )
  )
