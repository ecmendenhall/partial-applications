(ns partial-applications.ajax
  (:require [jayq.core :refer [$ children attr bind text data]]
            [goog.net.XhrIo :as gxhr]))

(defn load-more []
  (defn update-page [reply]
    (let [json-response (.getResponseJson (.-target reply))
          maindiv ($ :#main)
          strategy-p (children maindiv :.strategy)
          hexvalue-p (children maindiv :.hex)
          id (aget json-response "_id")
          strategy (aget json-response "strategy")]
      (text strategy-p strategy)
      (text hexvalue-p (str "0x" (.toString id 16))))
    nil)
  (gxhr/send "/json/" update-page)
  nil)

(defn add-listener []
  (-> ($ :#again)
    (attr "href" "#")
    (bind "click" load-more))
  nil)
