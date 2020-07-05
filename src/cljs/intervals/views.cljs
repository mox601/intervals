(ns intervals.views
  (:require
   [re-frame.core :as re-frame]
   [intervals.subs :as subs]
   [intervals.events :as events]))

(defn timer []
  (let [timer (re-frame/subscribe [::subs/timer])]
    [:div @timer]))

(defn start-button [duration repetitions]
  [:input {:type "button" :value "Start!"
           :on-click #(events/dispatch-start-timer duration repetitions)}])

;;TODO could receive interval-id to avoid reading id from db
(defn stop-button []
  [:input {:type "button" :value "Stop!"
           :on-click #(events/dispatch-stop-timer)}])

(defn main-panel []
  (let [name     (re-frame/subscribe [::subs/name])
        duration (re-frame/subscribe [::subs/duration])
        repetitions (re-frame/subscribe [::subs/repetitions])]
    [:div
     [:h1 "Hello from " @name "!"]
     [:div @duration " secs"] [:div @repetitions " repetitions"]

     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-duration-change-event inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-duration-change-event dec)}]

     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-repetition-change inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-repetition-change dec)}]
     
     ;;TODO start button enabled only if it's not started
     [start-button @duration @repetitions]
     ;;TODO stop button enabled only if it's started
     [stop-button]
     [timer]]))

