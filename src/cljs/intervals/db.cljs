(ns intervals.db
   (:require
    [intervals.timer :as timer]
    [intervals.tabata :as tabata]))

;;reframe question: subscriptions can receive a map, but component doesnt display all parts of the map
;; TODO merge maps or work with nested maps?
(def default-db
  {:name "Intervals"
   :tabata-form (tabata/init)
   :timer (timer/init)})

