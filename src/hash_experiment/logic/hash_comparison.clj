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

(defn average-accesses [insert-fn search-fn alpha]
  (let [n (int (* alpha m))]
    (->> (range trials)
         (map (fn [_]
                (let [keys (repeatedly n #(rand-int 10000))
                      table (reduce insert-fn (insert-fn nil m) keys)
                      accesses (map #(-> (search-fn table % m) :accesses) keys)]
                  (/ (reduce + accesses) n))))
         (reduce +)
         (/ trials))))

(def xs alpha-range)

(def ys-computed (map #(average-accesses logic.computed-chaining/insert logic.computed-chaining/search %) xs))
(def ys-explicit (map #(average-accesses logic.explicit-chaining/insert logic.explicit-chaining/search %) xs))
(def ys-double   (map #(average-accesses logic.double-hashing/insert logic.double-hashing/search %) xs))

(def chart (charts/xy-plot xs ys-computed
                           :title "Average Accesses vs Load Factor"
                           :x-label "Load Factor (α)"
                           :y-label "Average Accesses"))

(charts/add-lines chart xs ys-explicit :series-label "Explicit Chaining")
(charts/add-lines chart xs ys-double   :series-label "Double Hashing")
(charts/add-lines chart xs ys-computed :series-label "Computed Chaining")

(icore/view chart)
