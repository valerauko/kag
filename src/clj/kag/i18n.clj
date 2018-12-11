(ns kag.i18n
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [meta-merge.core :refer [meta-merge]]
            [tongue.core :as tongue]))

(def default-locale
  :en)

(def dicts
  (merge
    {:tongue/fallback default-locale}
    (let [i18n-path "resources/locales/"]
      (reduce
        (fn [aggr file]
          (if (and (.canRead file) (.isFile file))
            (let [pattern (re-pattern (str i18n-path "?([^/]+)/"))
                  language (->> file .getPath (re-find pattern) second keyword)]
              (->> file
                   io/reader
                   java.io.PushbackReader.
                   clojure.edn/read
                   (update aggr language meta-merge)))
            aggr))
        {}
        (rest (file-seq (io/file i18n-path)))))))

(def t
  (tongue/build-translate dicts))
