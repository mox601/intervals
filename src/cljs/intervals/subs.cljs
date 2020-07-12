(ns intervals.subs
  (:require
   [re-frame.core :as re-frame]
   [intervals.timer :as timer]
   [intervals.tabata :as tabata]))

;; subscriptions
;; allows views to observe data
(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

;; form state
(re-frame/reg-sub
 ::duration
 (fn [db]
   (tabata/duration (get db :tabata-form))))

(re-frame/reg-sub
 ::duration-rest
 (fn [db]
   (tabata/duration-rest (get db :tabata-form))))

(re-frame/reg-sub
 ::repetitions
 (fn [db]
   (tabata/repetitions (get db :tabata-form))))

;; timer remaining duration
(re-frame/reg-sub
 ::remaining-duration
 (fn [db]
   (timer/duration (db :timer))))

(re-frame/reg-sub
 ::remaining-repetitions
 (fn [db]
   (timer/repetitions (db :timer))))

