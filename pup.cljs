(.setFlagsFromString (js/require "v8") "--no-use_strict")

(def puppeteer (js/require "puppeteer"))
(def Q (js/require "q"))

(-> (Q (.launch puppeteer))
    (.then (fn [browser]
             (-> (Q (.newPage browser))
                 (.then (fn [page]
                          (.goto page "https://example.com/")
                          (.screenshot page #js {:path "example.png"})))
                 (.then #(.close browser))))))

#_(-> (.launch puppeteer) (.then #(.newPage %)) (.then #(do (.goto % "http://example.com")) (.screenshot % #js {:path "example.png"})))
