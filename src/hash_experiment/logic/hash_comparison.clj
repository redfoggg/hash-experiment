(ns hash-experiment.logic.hash-comparison
  (:require
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]
   [incanter.charts :as charts]))

(def m 997)
(def trials 10)
(def alpha-range (range 0.1 1.0 0.1))

(def x-values (range 1 11))
(def y-values (map #(* % %) x-values))

(defn create-line-plot 
  [x y]
  (charts/view (charts/line-chart x y)))

(create-line-plot x-values y-values)

