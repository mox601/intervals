(ns intervals.db
   (:require
   [intervals.timer :as timer]))

(def default-db
  {:name "Intervals"
   :duration 5
   :timer (timer/init)})

