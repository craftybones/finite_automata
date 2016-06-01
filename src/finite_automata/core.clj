(ns finite-automata.core
  (:require [clojure.set :as cset]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combi])
  (:gen-class))

(def char-to-digit (comp read-string str))

(defn state-set-reducer [from-delta]
  (fn [current-states letter]
    (apply cset/union
           (map #(from-delta [%1 letter]) current-states))))

(defn dfa [q alphabet delta q0 final-states]
  (let [from-delta (partial get-in delta)]
    (fn [string]
      (let [letters (map char-to-digit string)]
        (contains? final-states
                   (reduce #(from-delta [%1 %2]) q0 letters))))))

(defn nfa [q alphabet delta q0 final-states]
  (let [from-delta (partial get-in delta)]
    (fn [string]
      (let [letters (map char-to-digit string)
            init-states #{q0}
            reducer (state-set-reducer from-delta)]
        (cset/subset? final-states
                      (reduce reducer init-states letters))))))

(def alphabet #{0 1})
(def q #{:q1 :q2})
(def final-states #{:q1})
(def delta {:q1 {0 :q2 1 :q1} :q2 {0 :q2 1 :q1}})

(def ending-with-1 (dfa q alphabet delta :q1 final-states))

(def nfa-delta {:q1 {0 #{:q1} 1 #{:q1 :q2}} :q2 {1 #{:q3}}})
(def nfa-final-states #{:q3})

(def ending-with-two-ones (nfa q alphabet nfa-delta :q1 nfa-final-states))
