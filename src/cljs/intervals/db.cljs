(ns intervals.db
   (:require
   [intervals.timer :as timer]))

;; TODO refactor and wrap duration repetition in form timer
;;repetitions can't be less than 1
;;? subscriptions can receive a map, but component doesnt display all parts of the map
(def default-db
  {:name "Intervals"
   :duration 5 
   :repetitions 1
   :timer (timer/init)})

