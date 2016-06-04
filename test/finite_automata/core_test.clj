(ns finite-automata.core-test
  (:require [clojure.test :refer :all]
            [finite-automata.core :refer :all]))

(def states #{:q1 :q2})

(def alphabet-set #{0 1})

(def dfa-with (partial apply (partial dfa states alphabet-set)))

(defn tuple-from-map [{:keys [delta start-state final-states]}]
  [delta start-state final-states])

(def dfa-of (comp dfa-with tuple-from-map))

(def any-string
  {:delta {:q1 {1 :q1 0 :q1}}
   :start-state :q1
   :final-states #{:q1}})

(def at-least-one-zero
  {:delta {:q1 {1 :q1 0 :q2} :q2 {1 :q2 0 :q2}}
   :start-state :q1
   :final-states #{:q2}})

(deftest dfa-test
  (testing "single state dfa"
    (testing "any string"
      (let [d (dfa-of any-string)]
        (is (true? (d "")))
        (is (true? (d "0")))
        (is (true? (d "1")))
        (is (true? (d "00"))))))
  (testing "two-state-dfa"
    (testing "at least one zero"
      (let [d (dfa-of at-least-one-zero)]
        (is (true? (d "0")))
        (is (false? (d "1")))
        (is (true? (d "10")))
        (is (false? (d "11")))))))
