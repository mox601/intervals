(ns intervals.timer)

;; TODO throw if command can't be applied to state

;; queries
(defn interval-id
  [timer]
  (timer :interval-id))

(defn duration
  [timer]
  (timer :duration))

(defn repetitions
  [timer]
  (timer :repetitions))

(defn started?
  [timer]
  (= :started (timer :status)))

(defn- work-time?
  [timer]
  (= :work (timer :type)))

(defn- prepare-time?
  [timer]
  (= :prepare (timer :type)))

(defn more-repetitions?
  [timer]
   (> (repetitions timer) 1))

(defn expired?
  [timer]
  (and
   (not (more-repetitions? timer))
   (<= (duration timer)  0)
   (not (prepare-time? timer))))

;; TODO
;; from https://www.tabatatimer.com/
;; refactor model:
;; prepare
;; (work + rest) x cycles
;; tabatas (= cycles repetitions)
;; commands: pause-resume-stop

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

(defn start
  [timer duration duration-off repetitions interval-id]
  ;;TODO only a stopped/completed timer can be started
  (assoc timer
         :duration 2
         ;; TODO refactor as work-rest
         :initial-duration duration
         :initial-duration-off duration-off
         :type :prepare
         :status :started
         :repetitions repetitions
         :interval-id interval-id))

(defn- next-repetition
  [timer]
  (assoc timer
         :repetitions (dec (timer :repetitions))
         :duration (timer :initial-duration)
         :type :work))

(defn- off-time
  [timer]
  (assoc timer
         :duration (timer :initial-duration-off)
         :type :rest))

(defn- work-time
  [timer]
  (assoc timer
         :duration (timer :initial-duration)
         :type :work))

(defn- dec-duration
  [timer]
  (assoc timer
         :duration (dec (timer :duration))))

(defn decrement
  [timer]
  ;;TODO only a >= 0 counter and >= 0 repetitions can be decremented
  ;;TODO do we want to rest even when there's 1 repetition?
  (if (> (timer :duration) 0)
    (dec-duration timer)
    (if (prepare-time? timer)
      (work-time timer)
      (if (work-time? timer)
        (off-time timer)
        (if (more-repetitions? timer)
          (next-repetition timer)
          timer)))))

(defn stop
  [timer]
  ;;TODO only a started timer can be stopped
  (assoc timer
         :status :stopped
         :interval-id nil))

;;TODO? (throw (js/Error. "only a started timer with 0 duration and 0 repetitions can be completed"))
(defn complete
  [timer]
  (if (and
       (started? timer)
       (expired? timer))
    (assoc timer :status :completed :interval-id nil)
    timer))

