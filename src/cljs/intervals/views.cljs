(ns intervals.views
  (:require
   [re-frame.core :as re-frame]
   [intervals.subs :as subs]
   [intervals.events :as events]))

(defn duration []
  (let [remaining-duration (re-frame/subscribe [::subs/remaining-duration])]
    [:div @remaining-duration]))

;; TODO merge in 1 component?
(defn repetitions []
  (let [remaining-repetitions (re-frame/subscribe [::subs/remaining-repetitions])]
    [:div @remaining-repetitions]))

(defn start-button [duration duration-off repetitions]
  [:input {:type "button" :value "Start!"
           :on-click #(events/dispatch-start-timer duration duration-off repetitions)}])

;;TODO could receive interval-id to avoid reading id from db
(defn stop-button []
  [:input {:type "button" :value "Stop!"
           :on-click #(events/dispatch-stop-timer)}])

(defn main-panel []
    [:div
     (let [name (re-frame/subscribe [::subs/name])]
       [:h1 "Hello from " @name "!"])

     ;;TODO refactor as 1 reusable component
     (let [duration (re-frame/subscribe [::subs/duration])]
       [:div @duration " secs on"
        [:input {:type "button" :value "+"
                 :on-click #(events/dispatch-duration-change-event inc)}]
        [:input {:type "button" :value "-"
                 :on-click #(events/dispatch-duration-change-event dec)}]])

     (let [duration (re-frame/subscribe [::subs/duration-off])]
       [:div @duration " secs off"
        [:input {:type "button" :value "+"
                 :on-click #(events/dispatch-duration-off-change-event inc)}]
        [:input {:type "button" :value "-"
                 :on-click #(events/dispatch-duration-off-change-event dec)}]])
     
     (let [repetitions (re-frame/subscribe [::subs/repetitions])]
       [:div @repetitions " repetitions"
        [:input {:type "button" :value "+"
                 :on-click #(events/dispatch-repetition-change inc)}]
        [:input {:type "button" :value "-"
                 :on-click #(events/dispatch-repetition-change dec)}]])
     
     ;;TODO start button enabled only if it's not started
     (let [duration    (re-frame/subscribe [::subs/duration])
           duration-off (re-frame/subscribe [::subs/duration-off])
           repetitions (re-frame/subscribe [::subs/repetitions])]
       [start-button @duration @duration-off @repetitions])

     ;;TODO stop button enabled only if it's started
     [stop-button]
     [duration]
     [repetitions]])

