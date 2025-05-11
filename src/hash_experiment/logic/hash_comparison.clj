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

(def x (range 0.1 1.0 0.1))                      ;; valores no eixo X
(def y (map #(* % %) x))                         ;; exemplo: y = x^2

(def chart (charts/xy-plot x y
                           :title "Exemplo y = x^2"
                           :x-label "Alpha"
                           :y-label "Resultado"))

(icore/view chart)

