(ns aoc.p03
  (:require [clojure.string :as str]))

(def tree \#)
(def open-unvisited \.)
(def open-visited \O)

;; Read, left to right, up to down
;; Grid goes "infinitely" to the right
;;   use mod math to avoid actually expanding
;; Iterate a counter for trees
(def test-input "
#...
.#..
..#.
...#
")

(def test-input-2
  "..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#")


;; A map is a definite collect of infinite cycles.
;; The height of the slop is the legth of the collection


(defn parse-input [string-content]
  (->> string-content
       str/split-lines
       (filter #(not= "" %))
       (map cycle)))

(defn read-and-parse [fn]
  (-> fn slurp parse-input))

(def input (read-and-parse "data/input-p03-1.txt"))

;; WIP: Writing a fn to take current location, slope, map, and return new
;; location and the token there.
(defn get-next-loc [start slope hill]
  (let [[start-down-ind start-right-ind] start
        [n-down n-right] slope
        target-down (+ n-down start-down-ind)
        target-right (+ n-right start-right-ind)
        ridge (nthnext hill target-down)]
    [target-down target-right]))

(defn at-base-hill? [target-loc hill]
  ;; get length of hill collection
  ;; Compare to first coord in target loc, the height
  (let [target-down (first target-loc)
        hill-height-max (count hill)]
    (< target-down hill-height-max)))

(defn token-at-loc [[down right] hill]
  (nth (nth hill down) right))

(defn tree-at-loc? [[down right] hill]
  (if (at-base-hill? [down right] hill)
    (= tree
       (token-at-loc [down right] hill))
    false))

;; Depth first search
(defn count-trees-along-slope [hill slope]
  (loop [current-loc [0 0]
         end? (at-base-hill? current-loc hill)
         ntrees 0]
    (if-not end?
      ntrees
      (let [next-loc (get-next-loc current-loc slope hill)
            will-end? (at-base-hill? next-loc hill)]
        (recur
         ;; current-loc
         next-loc
         ;; end?
         will-end?
         ;; ntrees
         (if (tree-at-loc? next-loc hill)
           (inc ntrees)
           ntrees))))))


(defn p1 []
  (let [hill input slope [1 3]]
    (count-trees-along-slope hill slope)))

(defn p2 []
  (let [hill input
        slope-list [[1 1]
                    [1 3]
                    [1 5]
                    [1 7]
                    [2 1]]]
    (reduce * (map
               #(count-trees-along-slope hill %)
               slope-list))))

(comment
  (p1)
  (p2)

  (*
   (count-trees-along-slope input [1 1])
   (count-trees-along-slope input [1 3])
   (count-trees-along-slope input [1 5])
   (count-trees-along-slope input [1 7])
   (count-trees-along-slope input [2 1]))

  ;; (let [hill (parse-input test-input)

  ()

  (tree-at-loc? [1 4] (parse-input test-input-2)))
