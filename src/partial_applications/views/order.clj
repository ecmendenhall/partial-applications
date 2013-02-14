(ns partial-applications.views.order
  (:use [partial-applications.views.base :exclude [static-path]])
  (:require [hiccup.form :refer [email-field submit-button form-to]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "Order Cards"]
     [:p "Order cards!"]
     (form-to [:post "/email/"] 
              (email-field "email")
              (submit-button "Sign up"))
     (when (params :message) [:p (params :message)])])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
