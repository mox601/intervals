(ns intervals.events
  (:require
   [re-frame.core :as re-frame]
   [intervals.db :as db]
   [intervals.timer :as timer]
   [intervals.tabata :as tabata]
   [intervals.utils :as utils]))

(def interval-millis 500)

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

;; ~ service methods
;; timer events
(re-frame/reg-event-db
 :second
 (fn [db [_]]
   (if (timer/expired? (db :timer))
     (do
       (println "timer duration expired")
       (utils/clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/complete (db :timer))))
     (assoc db :timer (timer/decrement (db :timer))))))

;;form events
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
;; start (or resume)
;; TODO split start and resume
(re-frame/reg-event-db
 :start-timer
 (fn [db [_ duration duration-rest repetitions]]
   (if (timer/started? (db :timer))
     db
     (if (timer/paused? (db :timer))
       (let [interval-id (utils/set-interval (fn [] (re-frame/dispatch [:second])) interval-millis)]
         (assoc db :timer (timer/resume (db :timer) interval-id)))
       (let [interval-id (utils/set-interval (fn [] (re-frame/dispatch [:second])) interval-millis)]
         (assoc db :timer (timer/start (db :timer)
                                       {:initial-duration duration
                                        :initial-duration-off duration-rest
                                        :repetitions repetitions
                                        :interval-id interval-id})))))))

(re-frame/reg-event-db
 :resume-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     db
     (if (timer/paused? (db :timer))
       (let [interval-id (utils/set-interval (fn [] (re-frame/dispatch [:second])) interval-millis)]
         (assoc db :timer (timer/resume (db :timer) interval-id)))
       db))))

(re-frame/reg-event-db
 :stop-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (utils/clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/stop (db :timer))))
     db)))

(re-frame/reg-event-db
 :pause-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (utils/clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/pause (db :timer))))
     db)))

;; TODO wrap behind the tabata form APIs
(re-frame/reg-event-db
 :radio-selected
 (fn [db [_ id]]
   (assoc db :tabata-form (tabata/select (db :tabata-form) id))))

(defn id->event
  [id]
  ({:work :duration-change
    :rest :duration-rest-change
    :repetitions :repetition-change} id))

(re-frame/reg-event-fx
 :amount-changed
 (fn [{:keys [db]} [_ op]]
   {:db db
    :dispatch [(id->event (tabata/radio-selected (db :tabata-form))) op]
    }))

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

(defn dispatch-radio-selected
  [id]
  (re-frame/dispatch [:radio-selected id]))

(defn dispatch-amount-changed
  [op]
  (re-frame/dispatch [:amount-changed op]))

;; timer commands
(defn dispatch-start-timer
  [duration duration-rest repetitions]
  (re-frame/dispatch [:start-timer duration duration-rest repetitions]))

(defn dispatch-stop-timer
  []
  (re-frame/dispatch [:stop-timer]))

(defn dispatch-pause-timer
  []
  (re-frame/dispatch [:pause-timer]))

(defn dispatch-resume-timer
  []
  (re-frame/dispatch [:resume-timer]))

