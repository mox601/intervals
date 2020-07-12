(ns intervals.db
   (:require
    [intervals.timer :as timer]
    [intervals.tabata :as tabata]))

;; TODO refactor and wrap duration repetition in form timer
;;repetitions can't be less than 1
;;? subscriptions can receive a map, but component doesnt display all parts of the map
;; TODO merge maps or create keys?
(def default-db
  {:name "Intervals"
   :tabata-form (tabata/init)
   :timer (timer/init)})

