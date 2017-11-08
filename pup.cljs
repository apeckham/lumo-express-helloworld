(.setFlagsFromString (js/require "v8") "--no-use_strict")

(def puppeteer (js/require "puppeteer"))

(-> (.launch puppeteer)
    (.then (fn [browser]
             (-> (.newPage browser)
                 (.then (fn [page]
                          (.goto page "https://example.com/")
                          (.screenshot page #js {:path "example.png"})))
                 (.then #(.close browser)))))
    (.then #(js/console.log "done!")))
