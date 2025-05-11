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
    (->> (range trials)
         (map (fn [_]
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
                 {:success 0 :failure 0})
         (update :success #(/ % trials))
         (update :failure #(/ % trials)))))

(def xs alpha-range)

(def ys-computed (map #(average-success-failure-accesses logic.computed-chaining/insert logic.computed-chaining/search logic.computed-chaining/create-table %) xs))
(def ys-explicit (map #(average-success-failure-accesses logic.explicit-chaining/insert logic.explicit-chaining/search logic.explicit-chaining/create-table %) xs))
(def ys-double   (map #(average-success-failure-accesses logic.double-hashing/insert logic.double-hashing/search logic.double-hashing/create-table %) xs))

(def chart (charts/xy-plot xs ys-computed
                           :title "Average Accesses vs Load Factor"
                           :x-label "Load Factor (α)"
                           :y-label "Average Accesses"))

(charts/add-lines chart xs ys-explicit :series-label "Explicit Chaining")
(charts/add-lines chart xs ys-double   :series-label "Double Hashing")
(charts/add-lines chart xs ys-computed :series-label "Computed Chaining")

(icore/view chart)
