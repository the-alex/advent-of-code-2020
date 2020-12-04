(ns aoc.p04
  (:require [clojure.string :as str]
            [clojure.set :refer [difference]]))

(def required-kw #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid" "cid"})

(def test-input
  "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in")

(defn parse-input [string-content]
  (-> string-content
      str/split-lines))

(defn read-strings-until-newline [lines]
  "Given a list of strings, intended to be from str/split, read the collection
  until we find a newline. This newline indicates the end of one section of
  passport info."
  (loop [[this-line & remaining-lines] lines
         record-strings []]
    (if (empty? this-line)
      {:record-line (str/join " " record-strings) :remaining-lines remaining-lines}
      (recur
       remaining-lines
       (conj record-strings this-line)))))


(defn line->kw-set [line]
  (let [string-pairs (map #(str/split % #":") (str/split line #" "))
        present-keys (set (map first string-pairs))]
    present-keys))

(defn lines->kw-sets [lines]
  (loop [remaining-lines lines
         kw-sets []]
    (if (empty? remaining-lines)
      kw-sets
      (let [{record-line :record-line
             remaining-lines :remaining-lines}
            (read-strings-until-newline remaining-lines)
            kw-set (line->kw-set record-line)]
        (recur
         remaining-lines
         (cons kw-set kw-sets))))))

(defn valid-kw-set? [kw-set]
  (let [set-diff (difference required-kw kw-set)]
    ;; Set diff is empty -- all eight items present
    (or (empty? set-diff)
        ;; OR the only item is "cid" -- give a pass
        (not (empty? (difference #{"cid"} set-diff))))))

(defn count-valid-records [kw-sets]
  (->> kw-sets
       (map valid-kw-set?)
       (filter true?)
       count))

(comment

  (let [lines (str/split-lines test-input)]
    (count-valid-records (lines->kw-sets lines))
    #_(map count (lines->kw-sets lines))
    #_(lines->kw-sets lines))

  (let [lines (str/split-lines (slurp "data/input-p04-1.txt"))]
    #_(count-valid-records (lines->kw-sets lines))
    (last lines)
    )


  ;; 1 has all eight keys -- valid!
  (difference required-kw #{"hgt" "pid" "byr" "eyr" "iyr" "ecl" "cid" "hcl"})
  (valid-kw-set? #{"hgt" "pid" "byr" "eyr" "iyr" "ecl" "cid" "hcl"})

  ;; 2 is missing "hgt" -- invalid!
  (difference required-kw #{"pid" "byr" "eyr" "iyr" "ecl" "cid" "hcl"})
  (valid-kw-set? #{"pid" "byr" "eyr" "iyr" "ecl" "cid" "hcl"})

  ;; 3 missing cid, but we give it a pass -- valid!
  (difference required-kw #{"hgt" "pid" "byr" "eyr" "iyr" "ecl" "hcl"})
  (valid-kw-set? #{"hgt" "pid" "byr" "eyr" "iyr" "ecl" "hcl"})

  ;; NOTE Was `true`, now `false` issue was logical -- needed to wrap `empty?`
  ;; with `not`
  ;; 4 is missing cid and byr, so set diff is larger than cid -- invalid!
  (difference required-kw #{"hgt" "pid" "eyr" "iyr" "ecl" "hcl"})
  (valid-kw-set? #{"hgt" "pid" "eyr" "iyr" "ecl" "hcl"})

  (read-strings-until-newline (take 10 (str/split-lines test-input))))
