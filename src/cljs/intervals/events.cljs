(ns intervals.events
  (:require
   [re-frame.core :as re-frame]
   [intervals.db :as db]
   [intervals.timer :as timer]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

;; js functions wrappers
(defn clear-interval
  [interval-id]
  (js/clearInterval interval-id))

(defn set-interval
  [f millis]
  ;;TODO get and use also params
  (js/setInterval f millis))

;; timer events
(re-frame/reg-event-db
 :second
 (fn [db [_]]
   (if (timer/expired? (db :timer))
     (do
       (println "timer duration expired")
       (clear-interval (timer/interval-id (db :timer)))
       (assoc-in db [:timer] (timer/complete (db :timer))))
     (assoc-in db [:timer] (timer/decrement (db :timer))))))

;;form events
(re-frame/reg-event-db               
 :duration-change
 (fn [db [_ op]]
   (assoc db :duration (op (:duration db)))))

(re-frame/reg-event-db               
 :repetition-change
 (fn [db [_ op]]
   (assoc db :repetitions (op (:repetitions db)))))

;; timer events
(re-frame/reg-event-db
 :start-timer
 (fn [db [_ duration repetitions]]
   (if (timer/started? (db :timer))
     db
     (let [interval-id (set-interval (fn [] (re-frame/dispatch [:second])) 1000)]
       (assoc-in db [:timer] (timer/start (db :timer) duration repetitions interval-id))))))

(re-frame/reg-event-db
 :stop-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (clear-interval (timer/interval-id (db :timer)))
       (assoc-in db [:timer] (timer/stop (db :timer))))
     db)))

;; event dispatchers (commands from ui)
;;form commands
(defn dispatch-duration-change-event
  [op]
  (re-frame/dispatch [:duration-change op]))

(defn dispatch-repetition-change
  [op]
  (re-frame/dispatch [:repetition-change op]))

;; timer commands
(defn dispatch-start-timer
  [duration repetitions]
  (re-frame/dispatch [:start-timer duration repetitions]))

(defn dispatch-stop-timer
  []
  (re-frame/dispatch [:stop-timer]))

