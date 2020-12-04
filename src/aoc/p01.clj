(ns aoc.p01
  (:require [clojure.string :refer [split-lines]]
            [clojure.math.combinatorics :refer [combinations]]))


(defn all-pairs [coll]
  ;; When there is a `next`, let it bind to s
  (when-let [s (next coll)]
    (lazy-cat (for [y s] [(first coll) y])
              (all-pairs s))))

;; TODO Without a library to do the hard part for you? = P
(defn all-triples [coll]
  (combinations coll 3))

(defn coll->rec
  [pair]
  {:pair pair
   :sum (reduce + pair)
   :prod (reduce * pair)})

(defn p1 []
  (let [nums (parse-input "data/input-p01-1.txt")
        pair-records (map coll->rec (all-pairs nums))
        record-with-sum-2020 (first (filter #(= 2020 (:sum %)) pair-records))]
    record-with-sum-2020))


(defn parse-input [fn]
  (->> fn
    slurp
    split-lines
    (map read-string)))


;; TODO Thread everywhere
(defn p2 []
  (let [nums (parse-input "data/input-p01-1.txt")
        triple-records (map coll->rec (all-triples nums))
        record-with-sum-2020 (first (filter #(= 2020 (:sum %)) triple-records))]
    record-with-sum-2020))



(comment

  (p2)

  (def test-nums [1721 979 366 299 675 1456])
  (combinations [0 1 2 3 4 5] 3)


  (for [x [0 1 2 3 4 5]
        :let [y (* x 3)]
        :when (even? y)]
    {:x x :y y})

  ((comp :prod first)
        (for [pair (all-pairs-comb test-nums)
              :let [sum (reduce + pair)
                    prod (reduce * pair)]
              :when (= 2020 sum)]
          {:pair pair :sum sum :prod prod}))
   )
