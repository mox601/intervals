(ns intervals.tabata)

(defn init
  []
  {:duration 5
   :duration-rest 2
   :repetitions 1})

(defn duration
  [tabata-form]
  (tabata-form :duration))

(defn duration-rest
  [tabata-form]
  (tabata-form :duration-rest))

(defn repetitions
  [tabata-form]
  (tabata-form :repetitions))

(defn duration-change
  [tabata-form op]
  (assoc tabata-form :duration (op (:duration tabata-form))))

(defn duration-rest-change
  [db op]
  (assoc db :duration-rest (op (:duration-rest db))))

(defn repetition-change
  [db op]
  (assoc db :repetitions (op (:repetitions db))))

