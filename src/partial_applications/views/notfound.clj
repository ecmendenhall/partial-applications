(ns partial-applications.views.notfound
  (:use [partial-applications.views.base :exclude [static-path]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "404"]
     [:p "Two kinds of pages: Found, Not found."]])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
