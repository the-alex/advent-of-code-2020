(ns aoc.p04
  (:require [clojure.string :as str]))

(def required-kw #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid" #_"cid"})

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

(defn line->record [line]
  (let [string-pairs (map #(str/split % #":") (str/split line #" "))] string-pairs))

(comment

  (let [lines (str/split-lines test-input)]
    (loop [remaining-lines lines
           records []]
      (if (empty? lines)
        records
        (let [{record-line :record-line
               remaining-lines :remaining-lines}
              (read-strings-until-newline remaining-lines)
              parsed-record (line->record record-line)]
          (recur
           remaining-lines
           (cons parsed-record records))))))

  (read-strings-until-newline (take 10 (str/split-lines test-input)))

  ;; line->record
  (let [record-line "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"]
    (line->record record-line)))
