(defproject partial-applications "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [com.novemberain/monger "1.4.2"]
                 [clojurewerkz/spyglass "1.1.0-beta2"]
                 [metis "0.3.0"]
                 [lib-noir "0.3.5"]
                 [hiccup "1.0.2"]
                 [cheshire "5.0.1"]
                 [jayq "2.2.0"]]
  :plugins [[lein-ring "0.8.2"]
            [lein-cljsbuild "0.3.0"]]
  :min-lein-version "2.0.0"
  :cljsbuild {
    :builds [{
      :source-paths ["src/partial_applications/cljs"]
      :compiler {
        :output-to "resources/public/js/pa-main.js"
        :optimizations :whitespace
        :externs ["resources/jquery.js"]
        :pretty-print true}}]}
  :ring {:handler partial-applications.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
