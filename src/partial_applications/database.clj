(ns partial-applications.database
  (:require [monger.core :as mongo]
            [monger.collection :as collection]
            [monger.json]
            [cheshire.core :as cheshire]
            [clojurewerkz.spyglass.client :as memcache]))

(defn connect [collection]
  (mongo/connect!)
  (mongo/set-db! (mongo/get-db collection)))

(defn connect-memcache [server]
  (let [tmc-client (memcache/text-connection server)]
    tmc-client))

(defn cached-get [client get-func id]
  (let [cached-result (memcache/get client (str id))]
    (if (not (nil? cached-result))
      cached-result
      (let [result (get-func id)]
        (memcache/add client (str id) 43200 result)
        result))))

(defn cached-json-get [client get-func id]
  (let [json-id (str id "json")
        cached-result (memcache/get client json-id)]
    (if (not (nil? cached-result))
      cached-result
      (let [result (get-func id)]
        (memcache/add client json-id 43200 result)
        result))))

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

(defn get-all-json [collection]
  (cheshire/generate-string {:allStrategies (collection/find-maps collection)}))
