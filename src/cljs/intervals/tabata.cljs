(ns intervals.tabata)

(defn init
  []
  {:duration 5
   :duration-off 2
   :repetitions 1})

(defn duration
  [tabata-form]
  (tabata-form :duration))

(defn duration-change
  [tabata-form op]
  (assoc tabata-form :duration (op (:duration tabata-form))))

(defn duration-off-change
  [db op]
  (assoc db :duration-off (op (:duration-off db))))

(defn repetition-change
  [db op]
  (assoc db :repetitions (op (:repetitions db))))

