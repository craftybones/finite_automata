(ns finite-automata.nfa-test
  (:require [clojure.test :refer :all]
            [finite-automata.core :refer :all]))

(def states #{:q1 :q2})

(def alphabet-set #{0 1})

(defn from-delta-of [tuple] (rtransition-fn (:delta tuple)))

(def nfa-with (partial apply (partial nfa states alphabet-set)))

(defn tuple-from-map [{:keys [delta start-state final-states]}]
  [delta start-state final-states])

(def nfa-of (comp nfa-with tuple-from-map))

(def any-string
  {:delta {:q1 {1 #{:q1} 0 #{:q1}}}
   :start-state :q1
   :final-states #{:q1}})

(def only-zeroes
  {:delta {:q1 {0 #{:q1}}}
   :start-state :q1
   :final-states #{:q1}})

(def even-number-of-zeroes
  {:delta {:q1 {1 :q1 0 :q2} :q2 {1 :q2 0 :q1}}
   :start-state :q1
   :final-states #{:q1}})

(def ends-with-one
  {:delta {:q1 {1 :q1 0 :q2} :q2 {1 :q1 0 :q2}}
   :start-state :q1
   :final-states #{:q1}})

(def zeroes-ending-with-single-one
  {:delta {:q1 {0 #{:q1} 1 #{:q2}}}
   :start-state :q1
   :final-states #{:q2}})

(def single-zero-sandwich
  {:delta {:q1 {1 #{:q1} 0 #{:q2}} :q2 {1 #{:q2}}}
   :start-state :q1
   :final-states #{:q2}})

(def alternating-zeroes-beginning-with-zero
  {:delta {:q1 {0 #{:q2}} :q2 {0 #{:q1} 1 #{:q1}}}
   :start-state :q1
   :final-states #{:q2}})

(def zeroes-followed-by-ones
  {:delta {:q1 {0 #{:q1} :ε #{:q2}} :q2 {1 #{:q2}}}
   :start-state :q1
   :final-states #{:q2}})

(def zeroes-or-ones
  {:delta {:q1 {:ε #{:q2 :q3}} :q2 {0 #{:q2}} :q3 {1 #{:q3}}}
   :start-state :q1
   :final-states #{:q2 :q3}})

(def zeroes-followed-by-ones-vice-versa
  {:delta {:q1 {:ε #{:q2 :q4}}
           :q2 {0 #{:q2} :ε #{:q3}} :q3 {1 #{:q3}}
           :q4 {1 #{:q4} :ε #{:q5}} :q5 {0 #{:q5}}}
   :start-state :q1
   :final-states #{:q3 :q5}})

(def recursive-epslion-transitions
  {:delta {:q1 {:ε #{:q2}} :q2 {:ε #{:q3}}}
   :start-state :q1
   :final-states #{:q2 :q3}})

(def circular-epslion-transitions
  {:delta {:q1 {:ε #{:q2}} :q2 {:ε #{:q3}} :q3 {:ε #{:q1}}}
   :start-state :q1
   :final-states #{:q2 :q3}})

(deftest epsilon-test
  (testing "single epsilon transition between two states"
    (let [transition (from-delta-of zeroes-followed-by-ones)
          epsilons (partial all-epsilon-states transition)]
      (is (= #{:q2 :q1} (epsilons #{:q1})))
      (is (= #{:q2} (epsilons #{:q2})))))
  (testing "multiple epsilon transitions"
    (let [from-delta (from-delta-of recursive-epslion-transitions)
          epsilons (partial all-epsilon-states from-delta)]
      (is (= #{:q1 :q2 :q3} (epsilons #{:q1})))))
  (testing "circular epsilon transitions"
    (let [from-delta (from-delta-of circular-epslion-transitions)
          epsilons (partial all-epsilon-states from-delta)]
      (is (= #{:q1 :q2 :q3} (epsilons #{:q1}))))))

(deftest nfa-test
  (testing "single state nfa"
    (testing "any string"
      (let [n (nfa-of any-string)]
        (is (true? (n "")))
        (is (true? (n "0")))
        (is (true? (n "1")))
        (is (true? (n "00")))))
    (testing "only zeroes"
      (let [n (nfa-of only-zeroes)]
        (is (true? (n "")))
        (is (true? (n "0")))
        (is (true? (n "00")))
        (is (true? (n "000")))
        (is (false? (n "1")))
        (is (false? (n "01"))))))
  (testing "two state nfa(no epsilons)"
    (testing "zeroes ending with single one"
      (let [n (nfa-of zeroes-ending-with-single-one)]
        (is (true? (n "1")))
        (is (true? (n "01")))
        (is (true? (n "001")))
        (is (false? (n "0")))
        (is (false? (n "11")))
        (is (false? (n "101")))
        (is (false? (n "0011")))))
    (testing "zero sandwich"
      (let [n (nfa-of single-zero-sandwich)]
        (is (true? (n "101")))
        (is (true? (n "1101")))
        (is (true? (n "1011")))
        (is (true? (n "10")))
        (is (false? (n "100")))
        (is (false? (n "1001")))))
    (testing "alternating zeroes beginning with zero"
      (let [n (nfa-of alternating-zeroes-beginning-with-zero)]
        (is (true? (n "0")))
        (is (true? (n "010")))
        (is (true? (n "000")))
        (is (true? (n "01000")))
        (is (true? (n "01010"))))))
  (testing "two state nfa with epsilons"
    (testing "zeroes followed by ones"
      (let [n (nfa-of zeroes-followed-by-ones)]
        (is (true? (n "0")))
        (is (true? (n "01")))
        (is (true? (n "00")))
        (is (true? (n "001")))
        (is (true? (n "0011")))
        (is (true? (n "1")))
        (is (true? (n "11")))
        (is (false? (n "10")))
        (is (false? (n "010")))
        (is (false? (n "00110"))))))
  (testing "multiple state nfas with epsilons"
    (testing "zeros or ones"
      (let [n (nfa-of zeroes-or-ones)]
        (is (true? (n "1")))
        (is (true? (n "0")))
        (is (true? (n "11")))
        (is (true? (n "00")))
        (is (false? (n "10")))
        (is (false? (n "01")))))
    (testing "zeroes followed by ones or ones followed by zeroes"
      (let [n (nfa-of zeroes-followed-by-ones-vice-versa)]
        (is (true? (n "")))
        (is (true? (n "0")))
        (is (true? (n "1")))
        (is (true? (n "00")))
        (is (true? (n "11")))
        (is (true? (n "01")))
        (is (true? (n "10")))
        (is (true? (n "0011")))
        (is (true? (n "1100")))
        (is (false? (n "101")))
        (is (false? (n "010")))))))
