(ns finite-automata.nfa-dfa-test
  (:require [clojure.test :refer :all]
            [finite-automata.core :refer :all]
            [finite-automata.nfa-dfa :refer :all]))

(def states #{:q1})

(def alphabet-set #{0 1})

(defn from-delta-of [tuple] (rtransition-fn (:delta tuple)))

(def nfa-with (partial apply (partial nfa-to-dfa states alphabet-set)))

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


(deftest nfa-test
  (testing "single state nfa"
    (testing "any string"
      (let [n (nfa-of any-string)]
        (is (= 1 1))))))
