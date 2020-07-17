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
  ;;TODO get and use also other params
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
       (let [interval-id (set-interval (fn [] (re-frame/dispatch [:second])) 1000)]
         (assoc db :timer (timer/resume (db :timer) interval-id)))
       (let [interval-id (set-interval (fn [] (re-frame/dispatch [:second])) 1000)]
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
       (let [interval-id (set-interval (fn [] (re-frame/dispatch [:second])) 1000)]
         (assoc db :timer (timer/resume (db :timer) interval-id)))
       db))))

(re-frame/reg-event-db
 :stop-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/stop (db :timer))))
     db)))

(re-frame/reg-event-db
 :pause-timer
 (fn [db [_]]
   (if (timer/started? (db :timer))
     (do
       (clear-interval (timer/interval-id (db :timer)))
       (assoc db :timer (timer/pause (db :timer))))
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

(defn dispatch-pause-timer
  []
  (re-frame/dispatch [:pause-timer]))

(defn dispatch-resume-timer
  []
  (re-frame/dispatch [:resume-timer]))

