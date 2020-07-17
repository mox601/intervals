(ns intervals.timer)

;; TODO throw if command can't be applied to state

;; queries
(defn interval-id
  [m]
  (m :interval-id))

(defn duration
  [m]
  (m :duration))

(defn repetitions
  [m]
  (m :repetitions))

(defn started?
  [m]
  (= :started (m :status)))

(defn- work-time?
  [m]
  (= :work (m :type)))

(defn- prepare-time?
  [m]
  (= :prepare (m :type)))

(defn more-repetitions?
  [m]
   (> (repetitions m) 1))

(defn expired?
  [m]
  (and
   (not (more-repetitions? m))
   (<= (duration m)  0)
   (not (prepare-time? m))))

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
   ;;TODO keep track if duration is on or off
   :initial-duration 0
   :initial-duration-off 0
   :type nil
   :status :stopped
   :repetitions 1
   :interval-id nil})

;; destructure an input map instead of getting an array
;;TODO spec the start-command
(defn start
  [m start-command]
  ;;TODO only a stopped/completed timer can be started
  (let [{duration :initial-duration
         initial-duration-off :initial-duration-off
         repetitions :repetitions
         interval-id :interval-id} start-command]
    (assoc m
           :duration 2
           ;; TODO refactor as work-rest
           :initial-duration duration
           :initial-duration-off initial-duration-off
           :type :prepare
           :status :started
           :repetitions repetitions
           :interval-id interval-id)))

(defn- next-repetition
  [m]
  (assoc m
         :repetitions (dec (m :repetitions))
         :duration (m :initial-duration)
         :type :work))

(defn- off-time
  [m]
  (assoc m
         :duration (m :initial-duration-off)
         :type :rest))

(defn- work-time
  [m]
  (assoc m
         :duration (m :initial-duration)
         :type :work))

(defn- dec-duration
  [m]
  (assoc m
         :duration (dec (m :duration))))

(defn decrement
  [m]
  ;;TODO only a >= 0 counter and >= 0 repetitions can be decremented
  ;;TODO do we want to rest even when there's 1 repetition?
  (if (> (m :duration) 0)
    (dec-duration m)
    (if (prepare-time? m)
      (work-time m)
      (if (work-time? m)
        (off-time m)
        (if (more-repetitions? m)
          (next-repetition m)
          m)))))

(defn stop
  [m]
  ;;TODO only a started timer can be stopped
  (assoc m
         :status :stopped
         :interval-id nil))

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

