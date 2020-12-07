(ns aoc.p06
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn read-groups [string-content]
  (->> (str/split string-content #"\n\n")
       (map str/split-lines)
       (map #(map set %))))

(defn p1 [input-filename]
  (let [groups            (-> input-filename slurp read-groups)
        all-answers-per-group (map #(reduce set/union %) groups)]
    (reduce + (map count all-answers-per-group))))

(defn p2 [input-filename]
  (let [groups            (-> input-filename slurp read-groups)
        consensus-answers-per-group (map #(reduce set/intersection %) groups)]
    (reduce + (map count consensus-answers-per-group))))

(comment
  (p1 "data/input-p06.txt")
  (p2 "data/input-p06.txt"))
