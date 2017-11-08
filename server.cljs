(ns server.core
  (:require
   [cljs.nodejs :as nodejs]))

(defonce express (nodejs/require "express"))
(defonce http (nodejs/require "http"))

(def app (express))

(. app (get "/hello"
  (fn [req res]
    (.send res "hello world"))))

(doto (.createServer http #(app %1 %2))
  (.listen 3000))
