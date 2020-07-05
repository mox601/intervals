(ns intervals.timer)

;; TODO throw if command can't be applied to state

;; queries
(defn interval-id
  [timer]
  (timer :interval-id))

(defn duration
  [timer]
  (timer :duration))

(defn started?
  [timer]
  (= :started (timer :status)))

(defn expired?
  [timer]
  (<= (timer :duration)  0))

;; commands
(defn init
  []
  {:duration 0
   :status :stopped})

(defn start
  [timer duration interval-id]
  ;;TODO only a stopped/completed timer can be started
  (assoc timer :duration duration :status :started :interval-id interval-id))

(defn decrement
  [timer]
  ;;TODO only a >= 0 counter can be decremented
  (assoc timer :duration (- (timer :duration) 1)))

(defn stop
  [timer]
  ;;TODO only a started timer can be stopped
  (assoc timer :status :stopped :interval-id nil))

;;TODO (throw (js/Error. "only a started timer with 0 duration can be completed"))
(defn complete
  [timer]
  (if (and
       (started? timer)
       (expired? timer))
    (assoc timer :status :completed :interval-id nil)
    timer))

