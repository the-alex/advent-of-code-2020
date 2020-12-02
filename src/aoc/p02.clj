(ns aoc.p02
  (:require [clojure.string :as str]))

(def input (str/split-lines (slurp "data/input-p02-1.txt")))

(def test-input
"1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc")

(defn count-occurences [c coll]
  (count (filter #(= c % ) coll)))

(defn validate-password-p1 [in-str]
  (let [string-components    (str/split in-str #" ")
        predicate-range-pair (str/split (first string-components) #"\-" )
        predicate-range-l    (read-string (first predicate-range-pair))
        predicate-range-h    (read-string (second predicate-range-pair))
        predicate-character  (first (second string-components))
        password             (nth string-components 2)
        n-occurences         (count-occurences predicate-character password)
        valid?               (<= predicate-range-l n-occurences predicate-range-h)]
    valid?))

(defn p1 [] (count (filter true? (map validate-password-p1 input))))

(defn validate-password-p2 [in-str]
  (let [string-components    (str/split in-str #" ")
        predicate-range-pair (str/split (first string-components) #"\-" )
        predicate-range-l    (read-string (first predicate-range-pair))
        predicate-range-h    (read-string (second predicate-range-pair))
        predicate-character  (first (second string-components))
        password             (nth string-components 2)
        at-first-pos?        (= predicate-character (nth password (- predicate-range-l 1)))
        at-last-pos?         (= predicate-character (nth password (- predicate-range-h 1)))
        valid?               (not= at-first-pos? at-last-pos?)]
    valid?))


(defn p2 [] (count (filter true? (map validate-password-p2 input))))

(comment
  (p1)
  (p2)

  (every? letter-at-place )
  (map validate input)
  (count-occurences \a "aaabcde")
  (count (filter #(= \a %) "aaabcde"))

  (let [first-line           (first (str/split-lines test-input))
        string-components    (str/split first-line #" ")
        predicate-range-pair (str/split (first string-components) #"\-" )
        predicate-range-l    (read-string (first predicate-range-pair))
        predicate-range-h    (read-string (second predicate-range-pair))
        predicate-character  (first (second string-components))
        password             (nth string-components 2)
        n-occurences         (count-occurences predicate-character password)
        valid?               (<= predicate-range-l n-occurences predicate-range-h)
        ]
    valid?)

  (nth (re-find #"\w:" "123 123 123 z: foooooo") 0)
  (re-find #"\w+:" "123 123 123 z: foooooo")

  )
