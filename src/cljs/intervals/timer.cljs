(ns intervals.timer)

;; TODO throw if command can't be applied to state
;; https://cognitect.com/blog/2017/5/22/restate-your-ui-using-state-machines-to-simplify-user-interface-development make a map with the FSM transitions allowed use fsmviz to print graph

;; queries
(defn interval-id
  [m]
  (m :interval-id))

;;TODO
(defn duration
  [m]
  (m :duration))

(defn repetitions
  [m]
  (- (count (m :plan))
     (m :plan-index)))

(defn initial-repetitions
  [m]
  (count (m :plan)))

(defn started?
  [m]
  (= :started (m :status)))

(defn paused?
  [m]
  (= :paused (m :status)))

(defn running?
  [m]
  (or (started? m)
      (paused?  m)))

(defn stopped-or-completed?
  [m]
  (or
   (= :stopped   (m :status))
   (= :completed (m :status))))

;;TODO use a sub to show the current time type in the UI
(defn time-type
  [m]
  ((m :current-step) :type))

(defn more-steps?
  [m]
  (> (count (m :plan))
     (inc   (m :plan-index))))

(defn expired?
  [m]
  (and
   (not (more-steps? m))
   (<=  (m :duration)  0)))

;;TODO calculate remaining time
(defn remaining-duration
  [m]
  (println m)
  0)

;; TODO
;; from https://www.tabatatimer.com/
;; refactor model:
;; prepare
;; (work + rest) x cycles
;; tabatas (= cycles repetitions)
;; commands: pause-resume-stop

;;TODO repetitions can't be less than 1
;; commands
(defn init
  []
  {
   :duration 0
   :status :stopped
   :interval-id nil
   :plan nil
   :plan-index -1
   :current-step nil})

;; plan as data
  ;;TODO do we want to rest even when there's 1 repetition?
(defn plan
  [start-command]
  (let [{duration :initial-duration
         initial-duration-off :initial-duration-off
         repetitions :repetitions} start-command]
    ;; {:type :prepare
    ;;  :amount 2} ...
    ;;TODO clean up
    (if (< 1 repetitions)
      (vec (flatten (vec [{:type :prepare :amount 2}
                          (first (repeat (dec repetitions)
                                         [{:type :work :amount duration}
                                          {:type :rest :amount initial-duration-off}]))
                          {:type :work :amount duration}])))
      (vec [{:type :prepare :amount 2}
            {:type :work :amount duration}]))))

;; destructure an input map instead of getting an array
;;TODO spec the start-command
(defn start
  [m start-command]
  ;;TODO only a stopped/completed timer can be started
  (let [{interval-id :interval-id} start-command
        plan (plan start-command)
        first-step (first plan)]
    (assoc m
           :duration (first-step :amount) ;; TODO make prepare time configurable
           ;; TODO refactor as work-rest
           :status :started
           :interval-id interval-id
           :plan plan
           :plan-index 0
           :current-step first-step)))

(defn- next-step
  [m]
  (let [next-st (get (m :plan) (inc (m :plan-index)))]
    (assoc m
           :duration (next-st :amount)
           :plan-index (inc (m :plan-index))
           :current-step next-st)))

(defn- dec-duration
  [m]
  (assoc m
         :duration (dec (m :duration))))

(defn decrement
  [m]
  (if (> (m :duration) 0)
    (dec-duration m)
    (if (more-steps? m)
      (next-step m)
      m)))

(defn stop
  [m]
  ;;TODO only a started timer can be stopped
  (assoc m
         :status :stopped
         :interval-id nil))

(defn pause
  [m]
  ;;TODO only a started timer can be stopped
  (assoc m
         :status :paused
         :interval-id nil))

(defn resume
  [m interval-id]
  (assoc m
         :status :started
         :interval-id interval-id))

;;TODO? (throw (js/Error. "only a started timer with 0 duration and 0 repetitions can be completed"))
(defn complete
  [m]
  (if (and
       (started? m)
       (expired? m))
    (assoc m
           :status :completed
           :interval-id nil)
    m))

