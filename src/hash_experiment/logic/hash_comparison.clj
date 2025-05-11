(ns hash-experiment.logic.hash-comparison
  (:require
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]))

(def m 997)
(def trials 10)
(def alpha-range (range 0.1 1.0 0.1))

