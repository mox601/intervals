(ns intervals.timer)

;; todo throw if command can't be applied to state

;; commands

(defn init
  []
  {:duration 0
   :status :stopped})

(defn start
  [timer duration interval-id]
  ;;TODO only a stopped/completed timer can be started
  {:duration duration
   :status :started
   :interval-id interval-id})

(defn decrement
  [timer]
  ;;TODO only a >= 0 counter can be decremented
  (assoc-in timer [:duration] (- (timer :duration) 1)))

(defn stop
  [timer]
  ;;TODO only a started timer can be stopped
  {:duration (timer :duration)
   :status :stopped
   :interval-id nil})

(defn complete
  [timer]
  ;;TODO only a started timer with 0 duration can be completed
  {:duration 0
   :status :completed
   :interval-id nil})

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

