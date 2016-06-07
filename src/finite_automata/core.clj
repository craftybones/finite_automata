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

(defn state-set-reducer [transition]
  (fn [current-states letter]
    (apply cset/union
      (map (partial transition letter) current-states))))

(defn dfa [q alphabet delta q0 final-states]
  (let [tran (transition-fn delta)]
    (fn [string]
      (let [letters (to-digits string)]
        (contains? final-states
          (reduce tran q0 letters))))))

(defn nfa [q alphabet delta q0 final-states]
  (let [transition (rtransition-fn delta)
        epsilon-states (partial all-epsilon-states transition)
        reducer (state-set-reducer transition)
        epsilon-reducer (comp epsilon-states reducer)]
    (fn [string]
      (let [letters (to-digits string)
            init-states (epsilon-states #{q0})]
        (intersects?
          (reduce epsilon-reducer init-states letters)
          final-states)))))
