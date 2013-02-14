(ns partial-applications.ajax
  (:require [jayq.core :refer [$ children ajax attr bind text]]))

(defn load-more []
  (.log js/console "AJAX request")
  (defn update-page [json-response]
    (let [maindiv ($ :#main)
          strategy (children maindiv :.strategy)
          hexvalue (children maindiv :.hex)]
      (text strategy (.-strategy json-response))
      (text hexvalue (str "0x" (.toString (js/parseInt (.-_id json-response)) 16)))
    nil))
  (ajax "/json/" {:datatype "json" :success update-page})
  nil)

(defn add-listener []
  (-> ($ :#again)
    (attr "href" "#")
    (bind "click" load-more))
  nil)
