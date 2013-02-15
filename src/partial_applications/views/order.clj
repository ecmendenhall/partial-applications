(ns partial-applications.views.order
  (:use [partial-applications.views.base :exclude [static-path]])
  (:require [hiccup.form :refer [email-field submit-button form-to]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "Order Cards"]
     [:p "I'd like to print a deck of cards with all the Partial Applications strategies, but a
          high-quality print run requires a big minimum order. If you're interested in printed
          cards, leave your email below. If there's enough interest, I'll launch a Kickstarter
          and let you know. I will never send you spam."]
     (form-to {:id "email-form"} [:post "/email/"] 
              (email-field "email")
              (submit-button "Sign up"))
     (when (params :message) [:p (params :message)])])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
