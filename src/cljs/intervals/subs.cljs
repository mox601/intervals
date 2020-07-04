(ns intervals.subs
  (:require
   [re-frame.core :as re-frame]
   [intervals.timer :as timer]))

;; subscriptions
;; allow data to be observed from views
(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::duration
 (fn [db]
   (:duration db)))

(re-frame/reg-sub
 ::timer
 (fn [db]
   (timer/duration (db :timer))))

