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
 ::prepare
 (fn [db]
   (tabata/prepare (db :tabata-form))))

(re-frame/reg-sub
 ::duration
 (fn [db]
   (tabata/duration (db :tabata-form))))

(re-frame/reg-sub
 ::duration-rest
 (fn [db]
   (tabata/duration-rest (db :tabata-form))))

(re-frame/reg-sub
 ::repetitions
 (fn [db]
   (tabata/repetitions (db :tabata-form))))

;; timer remaining duration
(re-frame/reg-sub
 ::remaining-duration
 (fn [db]
   (timer/duration (db :timer))))

(re-frame/reg-sub
 ::time-type
 (fn [db]
   (timer/time-type (db :timer))))

;; timer remaining duration
(re-frame/reg-sub
 ::time-left
 (fn [db]
   (timer/remaining-duration (db :timer))))

(re-frame/reg-sub
 ::remaining-repetitions
 (fn [db]
   (timer/repetitions (db :timer))))

(re-frame/reg-sub
 ::started
 (fn [db]
   (timer/started? (db :timer))))

;;TODO review if can substitute some views subscribing to started
(re-frame/reg-sub
 ::running
 (fn [db]
   (timer/running? (db :timer))))

(re-frame/reg-sub
 ::paused
 (fn [db]
   (timer/paused? (db :timer))))

(re-frame/reg-sub
 ::stopped-or-completed
 (fn [db]
   (timer/stopped-or-completed? (db :timer))))

(re-frame/reg-sub
 ::initial-repetitions
 (fn [db]
   (timer/initial-repetitions (db :timer))))


