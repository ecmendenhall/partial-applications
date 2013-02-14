(ns partial-applications.views.about
  (:use [partial-applications.views.base :exclude [static-path]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "About"] 
     [:p ""]])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
