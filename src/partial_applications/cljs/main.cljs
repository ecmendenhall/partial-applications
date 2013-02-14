(ns partial-applications.main
  (:require
    [partial-applications.colors :refer [setup-menu]]
    [partial-applications.ajax :refer [add-listener]]
    [partial-applications.cursor :refer [start-cursor]] 
    [jayq.core :refer [document-ready]]))

(document-ready (fn []
                  (setup-menu)
                  (add-listener)
                  (start-cursor)))
