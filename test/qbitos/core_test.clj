(ns qbitos.core-test
  (:require [clojure.test :refer :all]
            [qbitos.complex :refer :all]))

(def A (complex 5 3))
(def B (complex 2 -9))
(def C [[[1 5],[-1 3]],[[0 2],[6 -1]]])
(def COL1 [[[1 1] [2 0]]])
(def COL2 [[[3 0] [4 0]]])
(def COL3 [[[5 0] [6 0]]])

(def RECT [[[1 1] [2 0]],[[1 1] [2 0]],[[1 1] [2 0]]])

(def CUA1 [[[1 0][2 0]][[3 0][4 0]]])
(def CUA2 [[[5 0][6 0]][[7 0][8 0]]])

(deftest sum-complex-numbers
  (testing "Sum of complex numbers."
    (is (= (sum A B) [7 -6]))))

(deftest square-complex-numbers
  (testing "Multiplication of complex numbers."
    (is (= (mul A A) [16 30]))))

(deftest conjugate-complex-numbers
  (testing "Conjugate of complex a number."
    (is (= (conjugate A) [5 -3]))))

(deftest conjugate-multiplication
  (testing "Multiplication by the conjugate."
    (is (= (mul A (conjugate A)) [34 0]))))

(deftest transposition
  (testing "Transposition of matrices"
    (is (= (trans RECT) [[[1 1] [1 1] [1 1]] [[2 0] [2 0] [2 0]]]))
    (is (= (trans COL1) [[[1 1]] [[2 0]]]))
    (is (= (trans C) [[[1 5] [0 2]] [[-1 3] [6 -1]]]))))

(deftest identity-generation
  (testing "Generation of an identity matrix."
    (is (= (ident 2) [[[1 0] [0 0]] [[0 0] [1 0]]]))))

(deftest null-generation
  (testing "Generation of a nu matrix."
    (is (= (null 2) [[[0 0] [0 0]] [[0 0] [0 0]]]))))

(deftest inv-generation
  (testing "Generation of an inverse identity matrix."
    (is (= (inv 2) [[[0 0] [1 0]] [[1 0] [0 0]]]))))

(deftest matrix-sum
  (testing "Sum of complex matrices."
    (is (= (msum C (ident 2)) [[[2 5] [-1 3]] [[0 2] [7 -1]]]))))

(deftest matrix-mul
  (testing "Multiplication of complex matrices."
    (is (= (mmul C (inv 2)) [[[-1 3] [1 5]] [[6 -1] [0 2]]]))))

(deftest tensor-product
  (testing "Tensor product of complex matrices."
    (is (= (tensorp COL1 COL2) [[[3 3][4 4][6 0][8 0]]]))))



