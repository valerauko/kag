(ns kag.handlers.oauth
  (:require [clojure.walk :refer [keywordize-keys]]
            [clojure.string :refer [split]]
            [clojure.set :refer [intersection]]
            [cljstache.core :refer [render-resource]]
            [kag.i18n :as i18n]))

(defn scope-explanation
  [input]
  (->> (split input #"\s+")
       (intersection #{"read" "write" "follow" "push"})
       sort
       (map
         #(i18n/t :en (keyword "oauth" %)))))

(defn auth-form
  [{{:keys [scope] :as query-params} :query-params :as req}]
  {:status 200
   :body (render-resource "templates/app_auth_form.html"
                          (merge query-params
                                 {:scopes (scope-explanation scope)}))
   :headers {"Content-type" "text/html"}})
