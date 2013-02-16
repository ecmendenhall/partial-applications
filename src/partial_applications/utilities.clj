(ns partial-applications.utilities
  (:require [clojure.string :as string]
            [partial-applications.database :as db]))

(defn read-in [filename]
  (db/connect (System/getenv "MONGOLAB_USER"))
  (let [strategies (string/split-lines (slurp filename))]
    (map (fn [i text] (db/add-strategy i text))
         (range 1 (inc (count strategies)))
         strategies)))
