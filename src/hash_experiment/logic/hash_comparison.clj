(ns hash-experiment.logic.hash-comparison
  (:require
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]
   [incanter.charts :as charts]
   [incanter.core :as icore]))

(def m 997)
(def trials 10)
(def alpha-range (range 0.1 1.0 0.1))

(defn average-success-failure-accesses
  [create-fn insert-fn search-fn alpha]
  (let [n (int (* alpha m))]
    (-> (range trials)
        (->> (map (fn [_]
                   (let [keys (repeatedly n #(rand-int 10000))
                         table (reduce insert-fn (create-fn m) keys)
                         success-accesses (map #(-> (search-fn table %) :accesses) keys)
                         fail-keys (repeatedly n #(rand-int 10000))
                         fail-accesses (map #(-> (search-fn table %) :accesses) fail-keys)]
                     {:success (/ (reduce + success-accesses) n)
                      :failure (/ (reduce + fail-accesses) n)})))
             (reduce (fn [acc x]
                      {:success (+ (:success acc) (:success x))
                       :failure (+ (:failure acc) (:failure x))})
                    {:success 0 :failure 0}))
        (update :success #(/ % trials))
        (update :failure #(/ % trials)))))

(def xs alpha-range)

(def results-computed (map #(average-success-failure-accesses 
                            logic.computed-chaining/create-table
                            logic.computed-chaining/insert
                            logic.computed-chaining/search %) xs))

(def results-explicit (map #(average-success-failure-accesses 
                            logic.explicit-chaining/create-table
                            logic.explicit-chaining/insert
                            logic.explicit-chaining/search %) xs))

(def results-double (map #(average-success-failure-accesses 
                          logic.double-hashing/create-table
                          logic.double-hashing/insert
                          logic.double-hashing/search %) xs))

(def success-computed (map :success results-computed))
(def failure-computed (map :failure results-computed))
(def success-explicit (map :success results-explicit))
(def failure-explicit (map :failure results-explicit))
(def success-double (map :success results-double))
(def failure-double (map :failure results-double))

(def success-chart (charts/xy-plot xs success-computed
                           :title "Successful Search: Average Accesses vs Load Factor"
                           :x-label "Load Factor (α)"
                           :y-label "Average Accesses"
                           :legend true))
(charts/add-lines success-chart xs success-explicit :series-label "Explicit Chaining")
(charts/add-lines success-chart xs success-double :series-label "Double Hashing")
(charts/add-lines success-chart xs success-computed :series-label "Computed Chaining")

(def failure-chart (charts/xy-plot xs failure-computed
                           :title "Unsuccessful Search: Average Accesses vs Load Factor"
                           :x-label "Load Factor (α)"
                           :y-label "Average Accesses"
                           :legend true))
(charts/add-lines failure-chart xs failure-explicit :series-label "Explicit Chaining")
(charts/add-lines failure-chart xs failure-double :series-label "Double Hashing")
(charts/add-lines failure-chart xs failure-computed :series-label "Computed Chaining")

(icore/view success-chart)
(icore/view failure-chart)
