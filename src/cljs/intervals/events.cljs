(ns intervals.events
  (:require
   [re-frame.core :as re-frame]
   [intervals.db :as db]
   [intervals.timer :as timer]
   [intervals.tabata :as tabata]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

;; js functions wrappers
(defn clear-interval
  [interval-id]
  (js/clearInterval interval-id))

;; TODO use https://github.com/daveyarwood/chronoid
(defn set-interval
  [f millis]
  ;;TODO get and use also params
  (js/setInterval f millis))

;; ~ service methods
;; timer events
(re-frame/reg-event-db
 :second
 (fn [db [_]]
   (if (timer/expired? (db :timer))
     (do
       (println "timer duration expired")
       (clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/complete (db :timer))))
     (assoc db :timer (timer/decrement (db :timer))))))

;;form events
;;TODO only positive numbers
(re-frame/reg-event-db               
 :duration-change
 (fn [db [_ op]]
  (assoc db :tabata-form (tabata/duration-change (db :tabata-form) op))))

(re-frame/reg-event-db               
 :duration-rest-change
 (fn [db [_ op]]
   (assoc db :tabata-form (tabata/duration-rest-change (db :tabata-form) op))))

(re-frame/reg-event-db               
 :repetition-change
 (fn [db [_ op]]
   (assoc db :tabata-form (tabata/repetition-change (db :tabata-form) op))))

;; timer events
(re-frame/reg-event-db
 :start-timer
 (fn [db [_ duration duration-rest repetitions]]
   (if (timer/started? (db :timer))
     db
     (let [interval-id (set-interval (fn [] (re-frame/dispatch [:second])) 1000)]
       (assoc-in db [:timer] (timer/start (db :timer) duration duration-rest repetitions interval-id))))))

(re-frame/reg-event-db
 :stop-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/stop (db :timer))))
     db)))

;; event dispatchers (commands from ui)
;; form commands
(defn dispatch-duration-change-event
  [op]
  (re-frame/dispatch [:duration-change op]))

(defn dispatch-duration-rest-change-event
  [op]
  (re-frame/dispatch [:duration-rest-change op]))

(defn dispatch-repetition-change
  [op]
  (re-frame/dispatch [:repetition-change op]))

;; timer commands
(defn dispatch-start-timer
  [duration duration-rest repetitions]
  (re-frame/dispatch [:start-timer duration duration-rest repetitions]))

(defn dispatch-stop-timer
  []
  (re-frame/dispatch [:stop-timer]))

