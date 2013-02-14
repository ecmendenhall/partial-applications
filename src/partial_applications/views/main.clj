(ns partial-applications.views.main
  (:require [partial-applications.views.base :refer [render-template]]))

(defn render-content [params]
  (let [strategy (:strategy params)
        id       (:id params)]
    [:div {:class "content large" :id "main"}
      [:p.strategy strategy]
      [:p.hex id]]))

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
