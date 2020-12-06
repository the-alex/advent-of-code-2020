(ns aoc.p05
  (:require [clojure.string :as str]
            [clojure.set :as set]))

;; 10 bits
;; first 7: binary for 128 rows
;;   F: upper half B: lower half
;; last 3: binary for 8 columns
(def test-input "FBFFBFFRLL
BFFFFBBRLR
FFFBBBBRLR
FBFFFBFLRL
FFBFBFFRRL
FFBBBBFRRR
FBFFBFBLRR
FFBBFBFRRL")

;; Decode:
;;  Split at ind 7 into string parts
;;  Replace characters with bit maps
;;  parse as int

(defn parse-row [line]
  (-> line
      (subs 0 7)
      (str/replace \F \0)
      (str/replace \B \1)
      (Integer/parseInt 2)))

(defn parse-col [line]
  (-> line
      (subs 7 10)
      (str/replace \L \0)
      (str/replace \R \1)
      (Integer/parseInt 2)))

(defn row-col->sid [row col]
  (+ col (* 8 row)))

(defn sid->row-col [sid]
  [(quot sid 8) (mod sid 8)])

(defn line->seat-record [line]
  (let [row (parse-row line)
        col (parse-col line)
        sid (row-col->sid row col)]
    {:row row
     :col col
     :sid sid}))

(defn lines->seat-records [lines]
  (map line->seat-record lines))

(defn p1 [input-filename]
  (let [lines (-> input-filename slurp str/split-lines)
        seat-records (lines->seat-records lines)]
    (apply max (map :sid seat-records))))


;; We know there are seats to either side of us, so we can't be the first or
;; last. Great, so lets check all numbers between the min and max sid
;;
;; first discontinuity is our seat.
;;
;; ... Or we could use set difference again, lol
(defn p2 [input-filename]
  (let [seat-ids (->> input-filename slurp str/split-lines lines->seat-records (map :sid) set)
        min-seat (apply min seat-ids)
        max-seat (apply max seat-ids)
        expected-seat-ids (set (range min-seat max-seat))
        remaining-seats (set/difference expected-seat-ids seat-ids)]
    (first remaining-seats)))

(comment
  (p2 "data/input-p05.txt")

  (set (range 51 851))

  ;; 52 => {:row 6 col 4}
  (let [row 6 col 4 sid (seat-id row col)]
    [(quot sid 8) (mod sid 8)])

  (Integer/parseInt "1111111" 2)
  (p1 "data/input-p05.txt")
  (let [seat-records [(map line->seat-record (-> test-input str/split-lines))]]
    (apply max (map :sid seat-records)))

  (let [lines (-> "data/input-p05.txt" slurp str/split-lines)
        seat-records (map line->seat-record lines)]
    #_(apply min (map :sid seat-records)) ;; => 51
    (identity (sort-by :sid seat-records)) ;; Starts at

    )
  (-> (str/replace \F \0) (str/replace \B \1) (Integer/parseInt 2))
  (subs "FBFFBFFRLL" 7 10)
  (Integer/parseInt "1111111" 2)
  (-> test-input str/split-lines #(take 2 %))
  (-> "data/input-p05.txt" slurp str/trim)
  (+ 1 2 3))
