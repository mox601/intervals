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

;; form state
(re-frame/reg-sub
 ::duration
 (fn [db]
   (:duration db)))

(re-frame/reg-sub
 ::repetitions
 (fn [db]
   (:repetitions db)))

;; timer remaining duration
;;TODO rename and add a sub for remaining repetitions
(re-frame/reg-sub
 ::remaining-duration
 (fn [db]
   (timer/duration (db :timer))))

(re-frame/reg-sub
 ::remaining-repetitions
 (fn [db]
   (timer/repetitions (db :timer))))

