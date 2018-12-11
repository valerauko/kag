(ns kag.routes
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [jsonista.core :as json]
            [reitit.ring :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->kebab-case-keyword
                                            ->snake_case_string]]
            [kag.handlers.oauth :as oauth]))

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

(defn transform-map
  ([hashmap] (transform-map hashmap ->kebab-case-keyword))
  ([hashmap func]
   (transform-keys
     #(if-not (number? %) (func %) %)
     hashmap)))

(defn case-wrapper
  [handler]
  (fn param-case [request]
    (let [response (-> request
                       (update :body-params transform-map)
                       (update :form-params transform-map)
                       (update :query-params transform-map)
                       handler)]
      response)))

(def router
  (ring/router
    ["/" {:get {:parameters {:query {:scope string?}}}
                :handler oauth/auth-form}]
    {:data {:middleware [wrap-logging
                         case-wrapper]}}))

(def wrapped-handler
  (-> (ring/ring-handler
        router
        (ring/routes
          (ring/create-resource-handler {:path "/public"})
          (ring/redirect-trailing-slash-handler)
          (ring/create-default-handler
            {:not-found (constantly {:status 404 :body "Not found"})})))
      default-middleware))
