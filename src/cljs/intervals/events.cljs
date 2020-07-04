(ns intervals.events
  (:require
   [re-frame.core :as re-frame]
   [intervals.db :as db]
   [intervals.timer :as timer]))

;; event handlers
(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn clear-interval
  [interval-id]
  (js/clearInterval interval-id))

(defn set-interval
  [f millis]
  ;;TODO get and use also params
  (js/setInterval f millis))

(re-frame/reg-event-db
 :second
 (fn [db [_]]
   (if (timer/expired? (db :timer))
     (do
       (println "timer duration expired")
       (clear-interval (timer/interval-id (db :timer)))
       (assoc-in db [:timer] (timer/complete (db :timer))))
     (assoc-in db [:timer] (timer/decrement (db :timer))))))

(re-frame/reg-event-db               
 :duration-change
 (fn [db [_ op]]
   (assoc db :duration (op (:duration db)))))

(re-frame/reg-event-db
 :start-timer
 (fn [db [_ duration]]
   (if (timer/started? (db :timer))
     db
     (let [interval-id (set-interval (fn [] (re-frame/dispatch [:second])) 1000)]
       (assoc-in db [:timer] (timer/start (db :timer) duration interval-id))))))

(re-frame/reg-event-db
 :stop-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (clear-interval (timer/interval-id (db :timer)))
       (assoc-in db [:timer] (timer/stop (db :timer))))
     db)))

;; event dispatchers (commands from ui)
(defn dispatch-duration-change-event
  [op]
  (re-frame/dispatch [:duration-change op]))

(defn dispatch-start-timer
  [duration]
  (re-frame/dispatch [:start-timer duration]))

(defn dispatch-stop-timer
  []
  (re-frame/dispatch [:stop-timer]))

