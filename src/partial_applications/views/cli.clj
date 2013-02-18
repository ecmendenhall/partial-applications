(ns partial-applications.views.cli
  (:use [partial-applications.views.base :exclude [static-path]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "CLI"]
     [:p "Why leave the terminal? If you've installed "
      [:a {:href "http://nodejs.org/"} "Node.js"] " and " [:a {:href "https://npmjs.org/"} "npm"] ", "
      "adding cryptic wisdom to your command line is as easy as:" 
      [:ul#cli [:li "$ npm install -g partialapps"] [:li"$ partialapps"] [:li "0xb5: Not changing, flowing."]]]])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
