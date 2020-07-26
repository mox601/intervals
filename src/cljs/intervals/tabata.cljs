(ns intervals.tabata)

;;TODO add selected radio and prepare value
(defn init
  []
  {:duration 3
   :duration-rest 2
   :repetitions 1
   :radio-selected :prepare})

(defn duration
  [m]
  (m :duration))

(defn duration-rest
  [m]
  (m :duration-rest))


(defn radio-selected
  [m]
  (m :radio-selected))

(defn repetitions
  [m]
  (m :repetitions))

;;TODO can't decrease to a number lower than 1
(defn duration-change
  [m op]
  (assoc m :duration (op (:duration m))))

;; can only select one of x values
(defn select
  [m id]
  (assoc m :radio-selected id))

;;TODO can't decrease to a number lower than 1
(defn duration-rest-change
  [m op]
  (assoc m :duration-rest (op (:duration-rest m))))

(defn repetition-change
  [m op]
  (assoc m :repetitions (op (:repetitions m))))

