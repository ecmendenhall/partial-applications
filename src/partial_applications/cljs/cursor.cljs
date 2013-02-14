(ns partial-applications.cursor
  (:require [jayq.core :refer [$ attr]]))

(defn start-cursor []
  (let [cursor ($ :#animate)]
    (defn blink! []
      (let [current-class (attr cursor "class")]
        (cond (= current-class "cursor") (attr cursor "class" "cursor on")
              (= current-class "cursor off") (attr cursor "class" "cursor on")
              (= current-class "cursor on") (attr cursor "class" "cursor off")))
      nil)
    (js/setInterval blink! 400))
  nil)
