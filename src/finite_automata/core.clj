(ns finite-automata.core
  (:require [clojure.set :as cset]
            [clojure.string :as str])
  (:gen-class))

(def char-to-digit (comp read-string str))

(defn state-set-reducer [from-delta]
  (fn [current-states letter]
    (apply cset/union
      (map #(cset/union
             (from-delta [%1 letter])
             (from-delta [%1 :ε])) current-states))))

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
