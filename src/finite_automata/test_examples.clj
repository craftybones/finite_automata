(ns finite-automata.test-examples
  (:require [finite-automata.core :as fa]
            [finite-automata.nfa-dfa :as conv]))

(def not-nil? (complement nil?))

(defn convert-epsilons
  [transition]
  (reduce-kv #(assoc %1 ({:e :ε} %2 %2) %3) {} transition))

(defn convert-delta
  [delta]
  (reduce-kv #(assoc %1 %2 (convert-epsilons %3)) {} delta))

(def automata-types {"dfa" fa/dfa
                     "nfa" fa/nfa
                     "nfa-to-dfa" conv/nfa-to-dfa})

(defn tuple-from-map
  [{:keys [states alphabets delta start-state final-states]}]
  [states alphabets (convert-delta delta) start-state final-states])

(defn create-fa
  [type tuple]
  (apply (automata-types type) (tuple-from-map tuple)))

(def none? (comp not some))

(defn run-fa-test
  [{:keys [name type tuple pass-cases fail-cases]}]
  (let [f (create-fa type tuple)]
    (hash-map
     :pass-cases ((group-by f pass-cases) false)
     :fail-cases ((group-by f fail-cases) true))))

(defn run-fa-tests [tests]
  (map #(hash-map
         :name (:name %1)
         :results (run-fa-test %1)) tests))

(defn all-failed [results]
  (filter #(or (not-nil? (get-in %1 [:results :pass-cases]))
               (not-nil? (get-in %1 [:results :fail-cases])))
          results))

(defn -main
  [filename]
  (let [tests (read-string (slurp filename))
        test-results (run-fa-tests tests)
        failed-tests (all-failed test-results)]
    (if (empty? failed-tests)
      (println "All tests passed!")
      (println failed-tests))
    (System/exit (count failed-tests))))
