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
(def range-max 50000)

(defn unique-random-numbers
  [n]
  (let [a-set (set (take n (repeatedly #(rand-int range-max))))]
    (concat a-set (set/difference (set (take n (range)))
                                  a-set))))

(defn average-success-accesses
  [create-fn insert-fn search-fn alpha]
  (let [n (int (* alpha m))]
    (->
     (->> (range trials)
          (map (fn [_]
                 (let [keys             (take n (unique-random-numbers n))
                       table            (reduce (fn [table k] (insert-fn table k m)) (create-fn m) keys)
                       success-accesses (map #(-> (search-fn table % m) :accesses) keys)]
                   {:success (/ (reduce + success-accesses) n)})))
          (reduce (fn [acc x]
                    {:success (+ (:success acc) (:success x))})
                  {:success 0}))
     (update :success #(/ % trials)))))

(defn average-success-accesses-computed-chaining
  [alpha]
  (let [n (int (* alpha m))
        keys (vec (take n (unique-random-numbers range-max)))
        table (loop [remaining-keys keys
                     current-table (logic.computed-chaining/create-table m)]
                (if (empty? remaining-keys)
                  current-table
                  (let [key (first remaining-keys)
                        result (logic.computed-chaining/insert current-table key m)]
                    (recur (rest remaining-keys) result))))
        accesses (map (fn [key]
                        (let [result (logic.computed-chaining/search table key m)]
                          (:accesses result)))
                      keys)]
    (if (seq accesses)
      {:success (/ (reduce + accesses) (count accesses))}
      {:success 0})))

(defn average-success-accesses-computed-chaining-trials
  [alpha]
  (let [results-from-each-trial (for [_ (range trials)]
                                  (let [n (int (* alpha m))]
                                    (if (zero? n)
                                      0.0
                                      (let [keys (take n (unique-random-numbers range-max))
                                            table (loop [remaining-keys keys
                                                         current-table (logic.computed-chaining/create-table m)]
                                                    (if (empty? remaining-keys)
                                                      current-table
                                                      (let [key-to-insert (first remaining-keys)
                                                            updated-table (logic.computed-chaining/insert current-table key-to-insert m)]
                                                        (recur (rest remaining-keys) updated-table))))
                                            accesses-list (map (fn [key-to-search]
                                                                 (let [search-result (logic.computed-chaining/search table key-to-search m)]
                                                                   (if (and search-result (contains? search-result :accesses))
                                                                     (:accesses search-result)
                                                                     m)))
                                                               keys)]
                                        (if (seq accesses-list)
                                          (double (/ (reduce + 0 accesses-list) (count accesses-list)))
                                          0.0)))))]
    (if (seq results-from-each-trial)
      {:success (double (/ (reduce + 0.0 results-from-each-trial) (count results-from-each-trial)))}
      {:success 0.0})))

(def results-computed (mapv average-success-accesses-computed-chaining-trials alpha-range))

#_(def results-computed (map #(average-success-accesses-computed-chaining
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

(def success-chart (charts/xy-plot alpha-range success-explicit
                                   :title "Successful Search: Average Accesses vs Load Factor"
                                   :x-label "Load Factor (α)"
                                   :y-label "Average Accesses"
                                   :legend true))
(charts/add-lines success-chart alpha-range success-explicit :series-label "Explicit Chaining")
(charts/add-lines success-chart alpha-range success-double :series-label "Double Hashing")
(charts/add-lines success-chart alpha-range success-computed :series-label "Computed Chaining")

(icore/view success-chart)

