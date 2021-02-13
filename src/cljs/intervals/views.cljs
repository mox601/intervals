(ns intervals.views
  (:require
   ;;TODO use cuerdas?
   [goog.string      :as gstring]
   [re-frame.core    :as re-frame]
   [intervals.subs   :as subs]
   [intervals.events :as events]))

(defn secs->str
  "seconds to a mm:ss string representation"
  [i]
  (str (gstring/format "%02d" (quot i 60))
       ":"
       (gstring/format "%02d" (mod i 60))))

(defn duration []
  (let [remaining-duration @(re-frame/subscribe [::subs/remaining-duration])]
    [:div (secs->str remaining-duration)]))

;; TODO merge in 1 component?
(defn repetitions []
  (let [remaining-repetitions @(re-frame/subscribe [::subs/remaining-repetitions])]
    [:div remaining-repetitions]))

;; TODO merge in 1 component?
(defn initial-repetitions []
  (let [initial-repetitions @(re-frame/subscribe [::subs/initial-repetitions])]
    [:div initial-repetitions]))

(defn start-button [duration duration-rest repetitions disabled]
  [:input {:type "button" :value "Start!" :class "f3"
           :on-click #(events/dispatch-start-timer duration duration-rest repetitions)
           :disabled disabled}])

(defn resume-button [disabled]
  [:input {:type "button" :value "Resume!" :class "f3"
           :on-click #(events/dispatch-resume-timer)
           :disabled disabled}])

;;TODO could receive interval-id to avoid reading id from db
(defn stop-button [disabled]
  [:input {:type "button" :value "Stop!" :class "f3"
           :on-click #(events/dispatch-stop-timer)
           :disabled disabled}])

(defn pause-button [disabled]
  [:input {:type "button" :value "Pause!" :class "f3"
           :on-click #(events/dispatch-pause-timer)
           :disabled disabled}])

;; input
;;TODO refactor as 1 reusable component?
(defn work-duration
  []
  ;;duration work
  (let [duration @(re-frame/subscribe [::subs/duration])]
    [:div (secs->str duration) " work"
     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-duration-change-event inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-duration-change-event dec)}]]))

(defn rest-duration
  []
  ;;duration rest
  (let [duration @(re-frame/subscribe [::subs/duration-rest])]
    [:div (secs->str duration) " rest"
     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-duration-rest-change-event inc)}]
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-duration-rest-change-event dec)}]]))

(defn repetitions-input
  []
  ;;repetitions
  (let [repetitions @(re-frame/subscribe [::subs/repetitions])]
    [:div repetitions " repetitions"
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

(defn setup-layout
  []
  ;;setup layout
  [:div {:class "helvetica tc f3"} "setup layout"
   [:form {:class "pa4 black-80"}
    
    [:fieldset {:class "bn"}
     
     [:div {:class "items-center mb2"}
      ;; TODO default selected
      [:input {:class "mr2" :type "radio" :name "v"
               :id :prepare :value "prepare"
               :on-change #(events/dispatch-radio-selected :prepare)
               }]
      [:label {:class "lh-copy" :for :prepare} "prepare "
       (let [prepare @(re-frame/subscribe [::subs/prepare])]
         [:span (secs->str prepare)])]]
     
     [:div {:class "items-center mb2"}
      [:input {:class "mr2" :type "radio" :name "v"
               :id :work :value "val"
               :on-change #(events/dispatch-radio-selected :work)}]
      [:label {:class "lh-copy" :for :work} "work "
       (let [duration @(re-frame/subscribe [::subs/duration])]
         [:span (secs->str duration)])]]
     
     [:div {:class "items-center mb2"}
      [:input {:class "mr2" :type "radio" :name "v"
               :id :rest :value "val"
               :on-change #(events/dispatch-radio-selected :rest)}]
      [:label {:class "lh-copy" :for :rest} "rest "
       (let [duration @(re-frame/subscribe [::subs/duration-rest])]
         [:span (secs->str duration)])]]
     
     [:div {:class "items-center mb2"}
      [:input {:class "mr2" :type "radio" :name "v"
               :id :repetitions :value "val"
               :on-change #(events/dispatch-radio-selected :repetitions)}]
      [:label {:class "lh-copy" :for :repetitions} "reps "
       (let [repetitions @(re-frame/subscribe [::subs/repetitions])]
         [:span repetitions])]]]
    
    [:div
     [:input {:type "button" :value "-"
              :on-click #(events/dispatch-amount-changed dec)}]
     [:input {:type "button" :value "+"
              :on-click #(events/dispatch-amount-changed inc)}]]
    
    [:div
     (let [duration      @(re-frame/subscribe [::subs/duration])
           duration-rest @(re-frame/subscribe [::subs/duration-rest])
           repetitions   @(re-frame/subscribe [::subs/repetitions])
           disabled      @(re-frame/subscribe [::subs/started])]
       [start-button duration duration-rest repetitions disabled])
     ]]])

(defn time-left
  []
  (let [left @(re-frame/subscribe [::subs/time-left])]
    [:div left]))

(defn time-type
  []
  (let [type @(re-frame/subscribe [::subs/time-type])]
    [:div type]))

(defn work-layout
  []
  ;;work layout
  [:div {:class "helvetica tc f3"} "work layout"
   ;;TODO show current type of interval (prepare, work, rest...)
   [:div {:class "f2"} "work"]
   [:div {:class "b f-subheadline"} [duration]]
   ;;TODO show time left
   [:div "time left: 5:23" [time-left]]
   [:div "time type " [time-type]]
   [:div {:class "f3"} "reps left: " [repetitions] " of " [initial-repetitions]]
   ;;[:input {:type "button" :value "pause" :class "f3"}]
   
   ;; TODO stop can be enabled also when paused
   (let [not-started (not @(re-frame/subscribe [::subs/started]))
         not-paused  (not @(re-frame/subscribe [::subs/paused]))]
     [:span
      [stop-button   not-started]
      [pause-button  not-started]
      [resume-button not-paused]
      ])
   
   ])

(defn main-panel []
  [:div
   
   (let [name @(re-frame/subscribe [::subs/name])]
     [:h1 "Hello from " name "!"])
   
     ;; only show input when stopped or completed
   (let [stopped-or-completed  @(re-frame/subscribe [::subs/stopped-or-completed])]
     (when stopped-or-completed
       [tabata-input]))
     
   ;; start button enabled only if it's not started
   (let [duration      @(re-frame/subscribe [::subs/duration])
         duration-rest @(re-frame/subscribe [::subs/duration-rest])
         repetitions   @(re-frame/subscribe [::subs/repetitions])
         disabled      @(re-frame/subscribe [::subs/started])]
     [start-button duration duration-rest repetitions disabled])
   
   ;; stop and pause buttons enabled only if it's started
   (let [disabled (not @(re-frame/subscribe [::subs/started]))]
     [:span
      [stop-button  disabled]
      [pause-button disabled]])
   
   (let [disabled (not @(re-frame/subscribe [::subs/paused]))]
     [resume-button disabled])
   
   ;; TODO only show when started or paused
   [duration]
   [repetitions]

   ;; setup/work layout
   (let [running? @(re-frame/subscribe [::subs/running])]
     (if running?
       [work-layout]
       [setup-layout])
     )

 
   
   ])

