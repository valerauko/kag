(ns kag.routes
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [jsonista.core :as json]
            [reitit.ring :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn wrap-logging
  [handler]
  (fn [{:keys [request-method uri remote-addr]
        {fwd-for :X-Forwarded-For} :headers
        {route :template} :reitit.core/match
        :as request}]
    (let [start (System/nanoTime)
          response (handler request)]
      (log/info (json/write-value-as-string
                  {:status (:status response)
                   :method request-method
                   :path uri
                   :route route
                   :remote-addr (or fwd-for remote-addr)
                   :response-time (/ (- (System/nanoTime) start) 1000000.0)}))
      response)))

(defn default-middleware
  [handler]
  (wrap-defaults handler site-defaults))

(def router
  (ring/router
    ["/" {:get (constantly {:status 200
                            :body (slurp (io/resource "public/index.html"))
                            :headers {"Content-type" "text/html"}})}]
    {:data {:middleware [wrap-logging]}}))

(def wrapped-handler
  (-> (ring/ring-handler
        router
        (ring/routes
          (ring/create-resource-handler {:path "/public"})
          (ring/redirect-trailing-slash-handler)
          (ring/create-default-handler
            {:not-found (constantly {:status 404 :body {:error "Not found"}})})))
      default-middleware))
