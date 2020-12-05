(ns aoc.p04
  (:require [clojure.string :as str]
            [clojure.set :refer [difference]]))

(defn parse-input [string-content]
  (-> string-content
      str/split-lines))

(defn next-passport-string [lines]
  "Given a list of strings, intended to be from str/split, read the collection
  until we find a newline. This newline indicates the end of one section of
  passport info."
  (loop [[this-line & remaining-lines] lines
         record-strings []]
    (if (empty? this-line)
      [(str/join " " record-strings) remaining-lines]
      (recur
       remaining-lines
       (conj record-strings this-line)))))

(defn line->passport [line]
  (let [string-pairs (map #(str/split % #":") (str/split line #" "))]
    (into {} string-pairs)))

(defn lines->passports [lines]
  (loop [remaining-lines lines
         passports []]
    (if (empty? remaining-lines)
      passports
      (let [[record-line remaining-lines] (next-passport-string remaining-lines)
            passport (line->passport record-line)]
        (recur
         remaining-lines
         (cons passport passports))))))


;; A passport is valid if it contains all the required keywords. Howver, if
;; missing only `cid`, then we'll count it as just fine.
(defn p1-is-valid? [passport]
  (let [required-keys #{"hgt" "pid" "byr" "eyr" "iyr" "ecl" "cid" "hcl"}
        present-keys (keys passport)
        missing-keys (difference required-keys present-keys)]
    ;; if there are no missing keys OR the only missing key is CID
    (or (empty? missing-keys) (= missing-keys #{"cid"}))))


(defn p1 [input-filepath]
  (->> input-filepath
       slurp
       str/split-lines
       lines->passports
       (filter p1-is-valid?)
       count))


(def validators
  {;; byr (Birth Year) - four digits; at least 1920 and at most 2002.
   "byr" (fn [value] (let [parsed-int (read-string value)] (<= 1920 parsed-int 2002)))

   ;; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
   "iyr" (fn [value] (let [parsed-int (read-string value)] (<= 2010 parsed-int 2020)))

   ;; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
   "eyr" (fn [value] (let [parsed-int (read-string value)] (<= 2020 parsed-int 2030)))

   ;; hgt (Height) - a number followed by either cm or in:
   ;; If cm, the number must be at least 150 and at most 193.
   ;; If in, the number must be at least 59 and at most 76.
   "hgt" (fn [value] (when-let
                      [matched-components (->> value (re-matches #"(\d+)((cm|in)+)"))]
                       (let [[_ magnitude unit] matched-components
                             magnitude        (read-string magnitude)]
                         (cond
                           (= unit "cm") (<= 150 magnitude 193)
                           (= unit "in") (<= 59 magnitude 76)
                           :else false))))

   ;; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
   "hcl" (fn [value] (->> value (re-find #"^#[a-f0-9]{6}$") nil? not))

   ;; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
   "ecl" (fn [value] (let [enums #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}]
                       (contains? enums value)))

   ;; pid (Passport ID) - a nine-digit number, including leading zeroes.
   "pid" (fn [value] (->> value (re-find #"^[0-9]{9}$") nil? not))

   ;; cid (Country ID) - ignored, missing or not.
   "cid" (fn [value] true)})

(comment
  (let [is-valid? (get validators "hgt")]
    (is-valid? "150cm")))

(defn p2-is-valid? [passport]
  (let [validation-results
        (for [field (keys validators)
              :let [is-valid? (get validators field)
                    value (get passport field)]
              :when (contains? passport field)]
          (is-valid? value))]
    (and
     ;; Every statement is true -- AND
     (every? true? validation-results)
     ;; there are at least seven keys found
     (<= 7 (count validation-results)))))

(defn p2 [input-filepath]
  (->> input-filepath
       slurp
       str/split-lines
       lines->passports
       (filter p2-is-valid?)
       count))

(comment

  (let [valid-passports (slurp "data/input-p04-valid-passports.txt")]

    (p1 "data/input-p04-1.txt")
    ;; => 222
    (p2 "data/input-p04-1.txt")
    ;; => 153

    (p2 "data/input-p04-invalid-passports.txt")
    ;; => 0
    (p2 "data/input-p04-valid-passports.txt")
    ;; => 4

    (->> valid-passports
         str/split-lines
         lines->passports
         (filter p2-is-valid?)
         count)

    (let [line "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"
          passport (line->passport line)]
      (let [present-keys (set (keys passport))] present-keys))

    (let [lines (str/split-lines (slurp "data/input-p04-1.txt"))]
      (lines->passports lines))

    (next-passport-string (take 5 (str/split-lines test-input)))))
