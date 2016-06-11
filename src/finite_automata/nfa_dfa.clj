(ns finite-automata.nfa-dfa
  (:require [clojure.set :as cset]
            [clojure.string :as str]
            [finite-automata.core :refer :all])
  (:gen-class))

(defn combine-with-group [combinations element]
  (let [list-of-element (set (list element))
        cross (partial cset/union list-of-element)]
    (concat combinations
            (map cross combinations))))

(def subsets (partial reduce combine-with-group [#{}]))

(def keywordify (comp keyword str/join vector))

(defn create-mapper [coll prefix]
  (into (hash-map)
	       (map-indexed (fn [idx itm]
                       [itm (keywordify prefix (inc idx))])
                     coll)))

(defn assoc-if-not-empty
  [coll [state alphabet transition]]
  (if (not transition)
    coll
    (assoc-in coll [state alphabet] transition)))

(defn table-to-map [table]
  (reduce assoc-if-not-empty {} table))

(defn create-new-transitions
  [mapper states alphabet reducer]
  (table-to-map
   (for [state states letter alphabet]
  	  [(mapper state)
      letter
      (mapper (reducer state letter))])))

(defn intersections [all-states final-states]
  (filter (partial intersects? final-states) all-states))

(defn nfa-to-dfa [q alphabet delta q0 final-states]
  (let [transition (rtransition-fn delta)
        list-of-new-states (subsets q)
        dfa-mapper (create-mapper list-of-new-states "x")
        new-states (into #{} (map dfa-mapper list-of-new-states))
        reducer (state-set-reducer transition)
        dfa-transition (create-new-transitions
                        dfa-mapper
                        list-of-new-states
                        alphabet
                        reducer)
        epsilon-states (partial all-epsilon-states transition)
        init-states (dfa-mapper (epsilon-states #{q0}))
        new-final-states (map dfa-mapper
                              (intersections list-of-new-states final-states))]
    (println dfa-mapper dfa-transition init-states new-final-states)))
