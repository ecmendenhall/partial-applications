(ns partial-applications.views.about
  (:use [partial-applications.views.base :exclude [static-path]]))

(defn static-path [filetype]
  (str "../" filetype "/"))

(defn render-content [params]
    [:div {:class "content small" :id "main"}
     [:h1 "About"] 
     [:p "Partial Applications is a set of questions, ideas, and aphorisms to inspire
          creative solutions to difficult programming problems. It's inspired by "
      [:a {:href "http://en.wikipedia.org/wiki/Peter_Schmidt_(artist)"} 
          "Peter Schmidt"] " and " 
      [:a {:href "http://en.wikipedia.org/wiki/Brian_Eno"} "Brian Eno"] "'s "
      [:a {:href "http://en.wikipedia.org/wiki/Oblique_Strategies"} "Oblique Strategies"] ", "
         "one of my favorite creative tools. (You can buy your own copy"
      [:a {:href "http://enoshop.co.uk/product/oblique-strategies"} " here"] ".)"]
     [:p "This site is written in " 
      [:a {:href "http://clojure.org/"} "Clojure"] " and " 
      [:a {:href "https://github.com/clojure/clojurescript"} "ClojureScript"] ". "
      "It's built on top of some wonderful libraries, including " 
      [:a {:href "https://github.com/weavejester/compojure"} "Compojure"] ", "
      [:a {:href "https://github.com/weavejester/hiccup"} "Hiccup"] ", " 
      [:a {:href "http://clojuremongodb.info/"} "Monger"] ", "
      [:a {:href "https://github.com/mylesmegyesi/metis"} "Metis"] ", and "
      [:a {:href "https://github.com/ibdknox/jayq"} "JayQ"] ". "
         "It's hosted on "
      [:a {:href "http://www.heroku.com/"} "Heroku"] " and uses a "  
      [:a {:href "http://www.mongodb.org/"} "MongoDB"] " backend."]
     [:p "All the source text for this project is available under a Creative Commons "
      [:a {:href "http://creativecommons.org/licenses/by-nc/3.0/"} "CC-BY-NC 3.0"]
        " license, so you can share and remix it however you'd like. Code I wrote is "
     [:a {:href "https://github.com/ecmendenhall/partial-applications" } " on Github "]
      "under an " 
     [:a {:href "http://opensource.org/licenses/MIT"} "MIT license"] "."]
     [:p [:a {:href "http://twitter.com/ecmendenhall"} "@ecmendenhall"]]])

(defn render [params]
  (let [content (render-content params)]
    (render-template params content)))
