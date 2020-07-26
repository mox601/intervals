(ns intervals.utils)

;; js functions wrappers
(defn clear-interval
  [interval-id]
  (js/clearInterval interval-id))

;; TODO use https://github.com/daveyarwood/chronoid
(defn set-interval
  [f millis]
  ;;TODO get and use also params
  (js/setInterval f millis))

