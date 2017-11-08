(ns server.core
  (:require
   [cljs.nodejs :as nodejs]))

(defonce express (nodejs/require "express"))
(defonce http (nodejs/require "http"))

(def app (express))

(.setFlagsFromString (js/require "v8") "--no-use_strict")
(defonce puppeteer (js/require "puppeteer"))
(defonce file-url (js/require "file-url"))

(. app (get "/hello"
  (fn [req res]
    (-> (.launch puppeteer)
        (.then (fn [browser]
                 (-> (.newPage browser)
                     (.then (fn [page]
                              (.goto page (file-url "page.html"))
                              (.screenshot page #js {:path "example.png"})))
                     (.then #(.close browser)))))
        (.then #(.sendFile res "example.png" #js {:root js/__dirname}))))))

(doto (.createServer http #(app %1 %2))
  (.listen 3000))
