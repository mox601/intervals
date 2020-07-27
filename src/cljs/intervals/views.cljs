(ns intervals.views
  (:require
   ;;TODO use cuerdas?
   [goog.string :as gstring]
   [re-frame.core :as re-frame]
   [intervals.subs :as subs]
   [intervals.events :as events]))

(defn secs->str
  "seconds to a mm:ss string representation"
  [i]
  (str (gstring/format "%02d" (quot i 60))
       ":"
       (gstring/format "%02d" (mod i 60))))

(defn duration []
  (let [remaining-duration (re-frame/subscribe [::subs/remaining-duration])]
    [:div (secs->str @remaining-duration)]))

;; TODO merge in 1 component?
(defn repetitions []
  (let [remaining-repetitions (re-frame/subscribe [::subs/remaining-repetitions])]
    [:div @remaining-repetitions]))

(defn start-button [duration duration-rest repetitions]
  [:input {:type "button" :value "Start!"
           :on-click #(events/dispatch-start-timer duration duration-rest repetitions)}])

(defn resume-button [disabled]
  [:input {:type "button" :value "Resume!"
           :on-click #(events/dispatch-resume-timer)
           :disabled disabled}])

;;TODO could receive interval-id to avoid reading id from db
(defn stop-button [disabled]
  [:input {:type "button" :value "Stop!"
           :on-click #(events/dispatch-stop-timer)
           :disabled disabled}])

(defn pause-button [disabled]
  [:input {:type "button" :value "Pause!"
           :on-click #(events/dispatch-pause-timer)
           :disabled disabled}])

;; input
;;TODO refactor as 1 reusable component?
(defn work-duration
  []
  ;;duration work
  (let [duration (re-frame/subscribe [::subs/duration])]
    [:div (secs->str @duration) " work"
     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-duration-change-event inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-duration-change-event dec)}]]))

(defn rest-duration
  []
  ;;duration rest
  (let [duration (re-frame/subscribe [::subs/duration-rest])]
    [:div (secs->str @duration) " rest"
     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-duration-rest-change-event inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-duration-rest-change-event dec)}]]))

(defn repetitions-input
  []
  ;;repetitions
  (let [repetitions (re-frame/subscribe [::subs/repetitions])]
    [:div @repetitions " repetitions"
     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-repetition-change inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-repetition-change dec)}]]))

(defn tabata-input
  []
  [:div
   [work-duration]
   [rest-duration]
   [repetitions-input]])

(defn main-panel []
  [:div
   
   (let [name (re-frame/subscribe [::subs/name])]
     [:h1 "Hello from " @name "!"])
   
     ;; only show input when stopped or completed
   (let [stopped-or-completed  @(re-frame/subscribe [::subs/stopped-or-completed])]
     (when stopped-or-completed
       [tabata-input]))
     
   ;;TODO start button enabled only if it's not started
   (let [duration (re-frame/subscribe [::subs/duration])
         duration-rest (re-frame/subscribe [::subs/duration-rest])
         repetitions (re-frame/subscribe [::subs/repetitions])]
     [start-button @duration @duration-rest @repetitions])
   
   ;; stop and pause buttons enabled only if it's started
   (let [disabled (not @(re-frame/subscribe [::subs/started]))]
     [:span
      [stop-button disabled]
      [pause-button disabled]])
   
   (let [disabled (not @(re-frame/subscribe [::subs/paused]))]
     [resume-button disabled])
   
   ;; TODO only show when started or paused
   [duration]
   [repetitions]
   

   ;;setup layout
   [:div {:class "helvetica tc f3"} "setup layout"
    [:form {:class "pa4 black-80"}
     
     [:fieldset {:class "bn"}
      
      [:div {:class "items-center mb2"}       
       ;;TODO selecting a radio changes a db state
       [:input {:class "mr2" :type "radio" :name "v"
                :id :prepare :value "prepare"
                :on-change #(println "TODO " :prepare)}]
       [:label {:class "lh-copy" :for :prepare} "prepare " [:span "00:10"]]]
      
      [:div {:class "items-center mb2"}
       [:input {:class "mr2" :type "radio" :name "v"
                :id :work :value "val"
                :on-change #(events/dispatch-radio-selected :work)}]
       [:label {:class "lh-copy" :for :work} "work "
        (let [duration (re-frame/subscribe [::subs/duration])]
          [:span (secs->str @duration)])]]
      
      [:div {:class "items-center mb2"}
       [:input {:class "mr2" :type "radio" :name "v"
                :id :rest :value "val"
                :on-change #(events/dispatch-radio-selected :rest)}]
       [:label {:class "lh-copy" :for :rest} "rest "
        (let [duration (re-frame/subscribe [::subs/duration-rest])]
        [:span (secs->str @duration)])]]
      
      [:div {:class "items-center mb2"}
       [:input {:class "mr2" :type "radio" :name "v"
                :id :repetitions :value "val"
                :on-change #(events/dispatch-radio-selected :repetitions)}]
       [:label {:class "lh-copy" :for :repetitions} "reps "
        (let [repetitions (re-frame/subscribe [::subs/repetitions])]
          [:span @repetitions])]]]
     
     [:div
      [:input {:type "button" :value "-"
               :on-click #(events/dispatch-amount-changed dec)}]
      [:input {:type "button" :value "+"
               :on-click #(events/dispatch-amount-changed inc)}]]
     
     [:div
      [:input {:type "button" :value "Start!"}]]]]

   ;;work layout
   [:div {:class "helvetica tc f3"} "work layout"
    [:div {:class "f2"} "work"]
    [:div {:class "b f-subheadline"} "00:23"]
    [:div "time left: 5:23"]
    [:div {:class "f3"} "reps left: 7 of 8"]
    [:input {:type "button" :value "pause" :class "f3"}]
    ]
   
   ])

