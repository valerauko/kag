(defproject social.kitsune/kag "0.1.0"
  :description "Frontend for Kitsune"
  :url "https://kitsune.social"
  :license {:name "GNU AFFERO GENERAL PUBLIC LICENSE"
            :url "https://www.gnu.org/licenses/agpl-3.0.en.html"}
  :min-lein-version "2.7.1"

  :dependencies [;clj
                 [org.clojure/clojure "1.9.0"]
                 ; cljs
                 [org.clojure/clojurescript "1.10.238"]
                 [org.clojure/core.async  "0.4.474"]
                 [reagent "0.7.0"] ]

  :main ^:skin-aot kag.core

  :plugins [[lein-figwheel "0.5.16"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :clean-targets ^{:protect false}
    [:target-path [:cljsbuild :builds :dev :compiler :output-dir]
                  [:cljsbuild :builds :dev :compiler :output-to]]


  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:uberjar {:omit-source true
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :cljsbuild
                         {:builds
                          {:min
                           {:source-paths ["src/cljs"]
                            :compiler {:output-dir "resources/public/js/compiled/"
                                       :output-to "resources/public/js/compiled/kag-prod.js"
                                       :main kag.core
                                       :optimizations :advanced
                                       :pretty-print false}}}}
                       :aot :all
                       :uberjar-name "kag.jar"}
             :dev {:dependencies [[binaryage/devtools "0.9.9"]
                                  [figwheel-sidecar "0.5.16"]
                                  [cider/piggieback "0.3.1"]]
                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                   :cljsbuild
                     {:builds
                      {:dev
                       {:source-paths ["src/cljs"]
                        :figwheel {:on-jsload "kag.core/on-js-reload"}
                        :compiler {:main "kag.core"
                                   :asset-path "/js/compiled/out"
                                   :output-to "resources/public/js/compiled/kag.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :preloads [devtools.preload]
                                   :source-map true
                                   :optimizations :none
                                   :pretty-print true}}}}
                   :source-paths ["env/dev/clj"]}})
