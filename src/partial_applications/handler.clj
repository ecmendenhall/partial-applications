(ns partial-applications.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [metis.core :as metis]
            [partial-applications.database :as db]
            [partial-applications.views.main :as main-view]
            [partial-applications.views.json :as json-view]
            [partial-applications.views.order :as order-view]
            [partial-applications.views.about :as about-view]))

(db/connect "strategies")

(metis/defvalidator email-validator
  [:email :email])

(defn get-strategy [n]
  (db/get-strategy n))

(defn random-strategy-number []
  (let [collection-size (int (db/count-coll "strategies"))]
    (inc (rand-int collection-size))))

(defn main-handler [_]
  (let [n        (random-strategy-number)
        hex-string (format "%#x" n)
        strategy (get-strategy n)        ]
      (main-view/render {:strategy strategy :id hex-string})))

(defn jsonapi-handler [_]
  (json-view/render {}))

(defn order-handler [_]
  (order-view/render {}))

(defn about-handler [_]
  (about-view/render {}))

(defn json-id-handler [id]
  (let [strategy-json (db/get-strategy-json (Integer/parseInt id))]
    {:headers {"Content-Type" "application/json"}
     :body strategy-json}))

(defn json-handler [_]
  (let [strategy-json (db/get-strategy-json (random-strategy-number))]
    {:headers {"Content-Type" "application/json"}
     :body strategy-json}))

(defn save-email [params]
  (let [email (params :email)]
  (if (empty? (email-validator {:email email})) 
    (do (db/add-email email)
        (order-view/render {:message "Thanks!"}))
    (order-view/render {}))))

(defroutes app-routes
  (GET "/" [] main-handler)
  (GET "/jsonapi/" [] jsonapi-handler)
  (GET "/order/" [] order-handler)
  (GET "/about/" [] about-handler)
  (GET "/json/" [] json-handler)
  (GET ["/json/id/:id/" :id #"[0-9]+"] [id] (json-id-handler id))
  (POST "/email/" {params :params} (save-email params))
  (route/resources "/css/" {:root "public/css"})
  (route/resources "/js/" {:root "public/js"})
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
