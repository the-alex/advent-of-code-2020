(ns aoc.p06
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn read-groups [string-content]
  (->> (str/split string-content #"\n\n")
       (map str/split-lines)
       (map #(map set %))))

(defn reduce-and-count [groups set-op]
  (reduce + (map count (map #(reduce set-op %) groups))))

(defn p1 [input-filename]
  (let [groups (-> input-filename slurp read-groups)]
    (reduce-and-count groups set/union)))

(defn p2 [input-filename]
  (let [groups (-> input-filename slurp read-groups)]
    (reduce-and-count groups set/intersection)))

(comment
  (p1 "data/input-p06.txt")
  (p2 "data/input-p06.txt"))
