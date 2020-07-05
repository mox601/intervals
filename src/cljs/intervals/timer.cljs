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

(defn more-repetitions?
  [timer]
   (> (repetitions timer) 1))

(defn expired?
  [timer]
  (and
   (not (more-repetitions? timer))
   (<= (duration timer)  0)))

;; commands
(defn init
  []
  {:duration 0
   :initial-duration 0
   :repetitions 1
   :status :stopped
   :interval-id nil})

(defn start
  [timer duration repetitions interval-id]
  ;;TODO only a stopped/completed timer can be started
  (assoc timer
         :duration duration
         :initial-duration duration
         :status :started
         :repetitions repetitions
         :interval-id interval-id))

(defn decrement
  [timer]
  ;;TODO only a >= 0 counter and >= 0 repetitions can be decremented
  (if (> (timer :duration) 0)
    (assoc timer :duration (dec (timer :duration)))
    (if (more-repetitions? timer)
      (assoc timer
             :repetitions (dec (timer :repetitions))
             :duration (timer :initial-duration))
      timer)))

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

