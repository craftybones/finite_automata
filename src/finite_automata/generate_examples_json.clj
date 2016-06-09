(ns finite-automata.generate-examples-json
  (:require [cheshire.core :refer :all]))

(defn -main
  [filename]
  (->  filename
       slurp
       read-string
       generate-string
       (generate-stream (clojure.java.io/writer "examples.json"))))
