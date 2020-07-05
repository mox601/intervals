(ns intervals.utils)

;; js functions wrappers
(defn clear-interval
  [interval-id]
  (js/clearInterval interval-id))

(defn set-interval
  [f millis]
  ;;TODO get and use also params
  (js/setInterval f millis))

