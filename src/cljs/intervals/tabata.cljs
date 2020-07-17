(ns intervals.tabata)

(defn init
  []
  {:duration 3
   :duration-rest 2
   :repetitions 1})

(defn duration
  [m]
  (m :duration))

(defn duration-rest
  [m]
  (m :duration-rest))

(defn repetitions
  [m]
  (m :repetitions))

;;TODO can't decrease to a number lower than 1
(defn duration-change
  [m op]
  (assoc m :duration (op (:duration m))))

;;TODO can't decrease to a number lower than 1
(defn duration-rest-change
  [m op]
  (assoc m :duration-rest (op (:duration-rest m))))

(defn repetition-change
  [m op]
  (assoc m :repetitions (op (:repetitions m))))

