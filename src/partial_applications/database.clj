(ns partial-applications.database
  (:require [monger.core :as mongo]
            [monger.collection :as collection]
            [monger.json]
            [cheshire.core :as cheshire]))

(defn connect [collection]
  (mongo/connect!)
  (mongo/set-db! (mongo/get-db collection)))

(defn count-coll [collection]
  (collection/count collection))

(defn get-strategy [n]
  ((collection/find-one-as-map "strategies" {:_id (int n)}) :strategy))

(defn get-strategy-json [n]
  (let [strategy (collection/find-one-as-map "strategies" 
                                             {:_id (int n)})]
    (cheshire/generate-string strategy)))

(defn add-strategy [id strategy]
  (collection/insert "strategies" {:_id (int id)
                                   :strategy strategy}))

(defn add-email [email]
  (when (empty? (collection/find-one-as-map "emails" {:email email}))
    (collection/insert "emails" {:email email})))
