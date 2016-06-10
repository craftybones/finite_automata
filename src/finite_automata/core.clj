(ns finite-automata.core
  (:require [clojure.set :as cset]
            [clojure.string :as str])
  (:gen-class))

(def char-to-digit (comp read-string str))
(def to-digits (partial map char-to-digit))
(def intersects? (comp not empty? cset/intersection))

(defn transition-fn [delta]
  (fn [& args]
    (get-in delta args)))

(defn rtransition-fn [delta]
  (fn [& args]
    (get-in delta (reverse args))))

(defn epsilons-of [transition states]
  (apply cset/union (map (partial transition :ε) states)))

(defn all-epsilon-states [transition states]
  (let [epsilons (epsilons-of transition states)]
    (if (cset/subset? epsilons states)
      states
      (recur transition (cset/union states epsilons)))))

(defn states-reachable-from [transition current-states letter]
  (apply cset/union
    (map (partial transition letter) current-states)))

(defn state-set-reducer [transition]
  (let [epsilons (partial all-epsilon-states transition)
        reachable-from (partial states-reachable-from transition)]
    (fn [current-states letter]
      (epsilons (reachable-from current-states letter)))))

(defn dfa [q alphabet delta q0 final-states]
  (let [tran (transition-fn delta)]
    (fn [string]
      (let [letters (to-digits string)]
        (contains? final-states
          (reduce tran q0 letters))))))

(defn nfa [q alphabet delta q0 final-states]
  (let [transition (rtransition-fn delta)
        epsilon-states (partial all-epsilon-states transition)
        init-states (epsilon-states #{q0})
        reducer (state-set-reducer transition)]
    (fn [string]
      (let [letters (to-digits string)]
        (intersects?
          (reduce reducer init-states letters)
          final-states)))))
