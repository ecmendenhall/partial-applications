(ns partial-applications.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [metis.core :as metis]
            [ring.adapter.jetty :refer [run-jetty]]
            [partial-applications.database :as db]
            [partial-applications.views.main :as main-view]
            [partial-applications.views.json :as json-view]
            [partial-applications.views.order :as order-view]
            [partial-applications.views.about :as about-view]
            [partial-applications.views.cli :as cli-view]
            [partial-applications.views.notfound :as notfound-view]))

(metis/defvalidator email-validator
  [:email :email])

(defn get-strategy [n]
  (db/cached-get db/cache db/get-strategy n))

(defn get-strategy-json [n]
  (db/cached-json-get db/cache db/get-strategy-json n))

(defn get-all-json [collection]
  (db/cached-json-get db/cache db/get-all-json collection))

(defn random-strategy-number []
  (let [collection-size (int (db/count-coll "strategies"))]
    (inc (rand-int collection-size))))

(defn main-handler [_]
  (let [n        (random-strategy-number)
        hex-string (format "%#x" n)
        strategy (get-strategy n)        ]
      (main-view/render {:strategy strategy :id hex-string :again true})))

(defn jsonapi-handler [_]
  (json-view/render {}))

(defn order-handler [_]
  (order-view/render {}))

(defn about-handler [_]
  (about-view/render {}))

(defn cli-handler [_]
  (cli-view/render {}))

(defn json-id-handler [id]
  (let [strategy-json (get-strategy-json (Integer/parseInt id))]
    {:headers {"Content-Type" "application/json"}
     :body strategy-json}))

(defn json-handler [_]
  (let [strategy-json (get-strategy-json (random-strategy-number))]
    {:headers {"Content-Type" "application/json"}
     :body strategy-json}))

(defn json-all-handler [_]
  (let [strategy-json (get-all-json "strategies")]
    {:headers {"Content-Type" "application/json"}
     :body strategy-json}))

(defn save-email [params]
  (let [email (params :email)]
  (if (empty? (email-validator {:email email})) 
    (do (db/add-email email)
        (order-view/render {:message "Thanks!"}))
    (order-view/render {}))))

(defn notfound-handler [_]
  (notfound-view/render {}))

(defroutes app-routes
  (GET "/" [] main-handler)
  (GET "/jsonapi/" [] jsonapi-handler)
  (GET "/order/" [] order-handler)
  (GET "/about/" [] about-handler)
  (GET "/json/" [] json-handler)
  (GET "/cli/" [] cli-handler)
  (GET ["/json/id/:id/" :id #"[0-9]+"] [id] (json-id-handler id))
  (GET ["/json/all/"] [] json-all-handler)
  (POST "/email/" {params :params} (save-email params))
  (route/resources "/css/" {:root "public/css"})
  (route/resources "/js/" {:root "public/js"})
  (route/not-found notfound-handler))

(def app
  (handler/site app-routes))

(defn start [port]
  (db/connect-dbs)
  (run-jetty app {:port port :join? false}))

(defn -main [& args]
  (let [port (Integer/parseInt 
               (or (System/getenv "PORT") "8080"))]
  (start port)))
