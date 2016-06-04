(ns finite-automata.nfa-test
  (:require [clojure.test :refer :all]
            [finite-automata.core :refer :all]))

(def states #{:q1 :q2})

(def alphabet-set #{0 1})

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
        (is (false? (n "1001")))))))
