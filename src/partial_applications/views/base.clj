(ns partial-applications.views.base
  (:require [hiccup.core :as hiccup]
            [hiccup.page :refer [html5 include-css include-js]]))

(def themes ["zenburn" "solarized-light" "solarized-dark"
             "monokai" "tomorrow" "tomorrow-night"])

(defn static-path [filetype]
  (str "/" filetype "/"))

(defn get-theme [themes]
  (nth themes (rand-int (count themes))))

(defn render-template [params content]
  (let [theme    (get-theme themes)]
    (html5 {:lang "en" :class theme}
      [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport" 
                :content "width=device-width, initial-scale=1.0"}]
        (include-css (str (static-path "css") "style.css"))
        (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js")
        (include-js  (str (static-path "js") "pa-main.js"))
        [:title "Partial Applications"]]
      [:body {:class theme}
        content
        [:footer
          [:div.statusline
            [:div.pull-left
             [:p.accent2 "Over 0x100 oblique ideas"]]
            [:div.pull-right
              [:p
               [:a#again {:href "/"}
                         "again"] 
                " < "
                [:a {:href "/about/"}
                    "about"] 
                " < " 
                [:a#colors {:href "/#"}
                           "colors"] 
                " < " 
                [:a {:href "/jsonapi/"}
                    "json"] 
                " < " 
                [:a {:href ""}
                    "cli"]]]
            [:div#order
             [:p [:a {:href "/order/"} ":OrderCards!"]]
             [:span {:class "cursor" :id "animate"}]]]]])))
