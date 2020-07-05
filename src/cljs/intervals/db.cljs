(ns intervals.db
   (:require
   [intervals.timer :as timer]))

;; TODO refactor and wrap duration repetition in form timer
;;repetitions can't be less than 1
(def default-db
  {:name "Intervals"
   :duration 5
   :repetitions 1
   :timer (timer/init)})

