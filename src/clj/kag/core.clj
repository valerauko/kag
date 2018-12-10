(ns kag.core
  (:require [clojure.tools.logging :as log]
            [aleph.http :as http]
            [mount.core :refer [defstate start stop]]
            [cprop.core :refer [load-config]]
            [cprop.source :refer [from-env from-system-props]]
            [kag.routes :as routes])
  (:gen-class))

(defstate config
  :start (load-config :merge [(from-env)
                              (from-system-props)]))

(defstate ^{:on-reload :noop} http-server
  :start
    (http/start-server
      routes/wrapped-handler
      {:port (get-in config [:server :port])
       :compression true})
  :stop
    (.close ^java.io.Closeable http-server))

(defn stop-server
  []
  (doseq [component (:stopped (stop))]
    (log/debug component "stopped"))
  (shutdown-agents))

(defn start-server
  []
  (doseq [component (:started (start))]
    (log/debug component "started"))
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. ^Runnable stop-server)))

(defn -main [& args]
  (cond
    (some #{"init"} args) (do ; TODO
                              (System/exit 0))
    :else (start-server)))
