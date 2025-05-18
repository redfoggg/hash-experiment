(ns hash-experiment.logic.hash-comparison
  (:require
   [clojure.set :as set]
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]
   [incanter.charts :as charts]
   [incanter.core :as icore]))

(def m 997)
(def trials 10)
(def alpha-range (range 0.1 1.0 0.1))
(def range-max 10000)

(defn unique-random-numbers
  [n]
  (let [a-set (set (take n (repeatedly #(rand-int n))))]
    (concat a-set (set/difference (set (take n (range)))
                                  a-set))))

(defn explicit-chaining-insert-fn
  [table k m keys]
  (reduce (fn [table k] (logic.explicit-chaining/insert table k m) (logic.explicit-chaining/create-table m))
          table
          keys))

(defn average-success-accesses
  [create-fn insert-fn search-fn alpha]
  (let [n (int (* alpha m))]
    (->
     (->> (range trials)
          (map (fn [_]
                 (let [keys             (take m (unique-random-numbers n))
                       table            (reduce (fn [table k] (insert-fn table k m)) (create-fn m) keys)
                       success-accesses (map #(-> (search-fn table % m) :accesses) keys)]
                   {:success (/ (reduce + success-accesses) n)})))
          (reduce (fn [acc x]
                    {:success (+ (:success acc) (:success x))})
                  {:success 0}))
     (update :success #(/ % trials)))))

(def results-computed (map #(average-success-accesses
                             logic.computed-chaining/create-table
                             logic.computed-chaining/insert
                             logic.computed-chaining/search %) alpha-range))

(def results-explicit (map #(average-success-accesses
                             logic.explicit-chaining/create-table
                             logic.explicit-chaining/insert
                             logic.explicit-chaining/search %) alpha-range))

(def results-double (map #(average-success-accesses
                           logic.double-hashing/create-table
                           logic.double-hashing/insert
                           logic.double-hashing/search %) alpha-range))

(def success-computed (map :success results-computed))
(def success-explicit (map :success results-explicit))
(def success-double (map :success results-double))

(def success-chart (charts/xy-plot alpha-range success-computed
                                   :title "Successful Search: Average Accesses vs Load Factor"
                                   :x-label "Load Factor (α)"
                                   :y-label "Average Accesses"
                                   :legend true))
(charts/add-lines success-chart alpha-range success-explicit :series-label "Explicit Chaining")
(charts/add-lines success-chart alpha-range success-double :series-label "Double Hashing")
(charts/add-lines success-chart alpha-range success-computed :series-label "Computed Chaining")

(icore/view success-chart)

