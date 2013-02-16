(ns partial-applications.views.json
  (:use [partial-applications.views.base :exclude [static-path]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "JSON"]
     [:p "If you are a robot who reads only serialized data, or you'd like to build something
          interesting using these strategies, simply GET one of the following URLs:"
          [:ul [:li [:p [:a {:href "http://partialapplications.herokuapp.com/json/"}
                        "http://partialapplications.herokuapp.com/json/"]] 
                    [:p "Get a random strategy."]]
               [:li [:p [:a {:href "http://partialapplications.herokuapp.com/json/id/5/"}
                        "http://partialapplications.herokuapp.com/json/id/:n/"]]
                    [:p "Get a strategy by ID (Hex ID stored as a decimal integer)"]]
               [:li [:p [:a {:href "http://partialapplications.herokuapp.com/json/all/"}
                        "http://partialapplications.herokuapp.com/json/all/"]]
                    [:p "Get all available strategies."]]]]])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
