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

(defn reducer-from [delta]
  (state-set-reducer (rtransition-fn delta)))

(defn states-reachable-via-epsilon [delta]
  (partial all-epsilon-states (rtransition-fn delta)))

(def setify (partial into #{}))

(defn nfa-to-dfa [q alphabet delta q0 final-states]
  (let [reducer (reducer-from delta)
				epsilon-states (states-reachable-via-epsilon delta)
        new-states (subsets q)
        dfa-map (create-mapper new-states "x")
        dfa-mapper (comp setify (partial map dfa-map))
        q0-dfa (dfa-map (epsilon-states #{q0}))
        q-dfa (dfa-mapper new-states)
        delta-dfa (create-new-transitions
                        dfa-map
                        new-states
                        alphabet
                        reducer)
        final-states-dfa (dfa-mapper
                          (intersections new-states final-states))]
    (dfa q-dfa alphabet delta-dfa q0-dfa final-states-dfa)))
