(ns finite-automata.core
  (:require [clojure.set :as cset]
            [clojure.string :as str])
  (:gen-class))

(def char-to-digit (comp read-string str))
(def to-digits (partial map char-to-digit))

(defn all-epsilon-states [from-delta states]
  (cset/union states
              (apply cset/union (map #(from-delta [%1 :ε]) states))))

(defn state-set-reducer [from-delta]
  (fn [current-states letter]
    (apply cset/union
      (map #(from-delta [%1 letter]) current-states))))

(defn dfa [q alphabet delta q0 final-states]
  (let [from-delta (partial get-in delta)]
    (fn [string]
      (let [letters (to-digits string)]
        (contains? final-states
          (reduce #(from-delta [%1 %2]) q0 letters))))))

(defn nfa [q alphabet delta q0 final-states]
  (let [from-delta (partial get-in delta)
        epsilon-states (partial all-epsilon-states from-delta)]
    (fn [string]
      (let [letters (to-digits string)
            init-states #{q0}
            reducer (state-set-reducer from-delta)
            epsilon-reducer (comp epsilon-states reducer epsilon-states)]
        (cset/subset? final-states
                      (reduce epsilon-reducer init-states letters))))))
