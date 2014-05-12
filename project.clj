(defproject circles "0.1.0-SNAPSHOT"
  :description "click on the rectangle to add circles"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]]

  :plugins [[lein-cljsbuild "1.0.2"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "circles"
              :source-paths ["src"]
              :compiler {
                :output-to "circles.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
