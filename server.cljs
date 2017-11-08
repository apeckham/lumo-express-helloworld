(ns server.core
  (:require
   [cljs.nodejs :as nodejs]))

(defonce express (nodejs/require "express"))
(defonce http (nodejs/require "http"))

(def app (express))

(.setFlagsFromString (js/require "v8") "--no-use_strict")
(defonce puppeteer (js/require "puppeteer"))
(defonce file-url (js/require "file-url"))
(defonce tmp (js/require "tmp"))

(. app (get "/hello"
  (fn [req res]
    (.file tmp #js {:postfix ".png"} (fn [err tmp-path]
                 (-> (.launch puppeteer)
                     (.then (fn [browser]
                              (-> (.newPage browser)
                                  (.then (fn [page]
                                           (.setViewport page #js {:width 2000 :height 2000})
                                           (-> (.goto page (file-url "page.html") #js {:waitUntil "networkidle0"})
                                               (.then #(.screenshot page #js {:path tmp-path})))))
                                  (.then #(.close browser))
                                  (.catch #(js/console.log %)))))
                     (.then #(.sendFile res tmp-path))
                     (.catch #(js/console.log %))))))))

(doto (.createServer http #(app %1 %2))
  (.listen 3000))
