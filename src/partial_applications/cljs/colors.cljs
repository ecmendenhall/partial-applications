(ns partial-applications.colors
  (:require [jayq.core :as jq]))

(def colors ["zenburn" 
             "solarized light" 
             "solarized dark" 
             "tomorrow" 
             "tomorrow night" 
             "monokai"])

(defn change-colorscheme [colorscheme]
  (.setAttribute (.-body js/document) "class" (.replace colorscheme " " "-")))

(defn save-colorscheme [colorscheme]
  (.setItem js/localStorage "colorscheme" colorscheme)
  (change-colorscheme colorscheme))

(defn get-colorscheme []
  (let [colorscheme (.getItem js/localStorage "colorscheme")]
    (when (not (nil? colorscheme))
      (change-colorscheme colorscheme))))

(defn show-menu []
  (-> (jq/$ :#colormenu)
      (jq/attr "class" "visible")))

(defn hide-menu []
  (-> (jq/$ :#colormenu)
      (jq/attr "class" "hidden")))

(defn create-scheme-link [colorscheme]
  (let [new-link (.createElement js/document "a")]
  (-> (jq/$ new-link)
      (jq/attr "id" colorscheme)
      (jq/attr "href" "#")
      (jq/text colorscheme)
      (jq/bind "click" 
                (fn [] (save-colorscheme colorscheme)
                      (hide-menu))))
    new-link))

(defn create-scheme-li [a]
  (let [new-li (.createElement js/document "li")]
    (.appendChild new-li a)
    new-li))

(defn add-color-menu []
  (let [main   (jq/$ :#main)
        colordiv     (.createElement js/document "div")
        colorlist    (.createElement js/document "ul")]
    (mapv (fn [li] (.appendChild colorlist li))
          (mapv create-scheme-li
                (mapv create-scheme-link colors)))
    (.appendChild colordiv colorlist)
    (.setAttribute colordiv "id" "colormenu")
    (.setAttribute colordiv "class" "hidden")
    (jq/append main colordiv)))

(defn setup-menu []
  (get-colorscheme)
  (add-color-menu)
  (-> (jq/$ :#colors)
      (jq/bind "click" show-menu)))
