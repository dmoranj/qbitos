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
(defoperator X1 2)
(defoperator X0 3)

(deftest multibit-definition
  (testing "Definition of multibit constants."
    (is (= |10> [[[0 0]] [[0 0]] [[1 0]] [[0 0]]]))))

(deftest number-operator
  (testing "Number operator conditions."
    (is (= (mmul n n) n))
    (is (= (mmul nn nn) nn))
    (is (= (mmul nn n) (mmul n nn)))
    (is (= (mmul nn n) [[[0 0] [0 0]] [[0 0] [0 0]]]))
    (is (= (msum n nn) (ident 2)))))

(deftest operator-combination
  (testing "Operator combination macro"
    (is (= (mmul X1 |10>) |11>))
    (is (= (mmul X1 |01>) |00>))
    (is (= (mmul X1 |10>) |11>))
    (is (= (mmul X0 |010>) |110>))
    ))
