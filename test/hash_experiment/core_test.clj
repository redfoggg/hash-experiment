(ns hash-experiment.core-test
  (:require [clojure.test :refer [deftest is]]
            [hash-experiment.logic.double-hashing :as logic.double-hashing]
            [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]))

(def double-hashing-test-insertions
  [27 18 29 43 77 16 40 49 5])

(def double-hash-expected-result
  [77 nil 40 49 nil
   27 16 18 5
   29 43])

(def explicit-chaining-test-insertions
  [27 18 29 43 77 13 16 40])

(def explicit-chaining-expected-result
  ['(77) '() '(13) '() '() '(16 27) '() '(40 29 18) '() '() '(43)])

(deftest double-hashing-bulk-test
  (let [empty-table (vec (repeat 11 nil))
        result (reduce (fn [table k] (logic.double-hashing/insert table k))
                       empty-table
                       double-hashing-test-insertions)]
    (is (= double-hash-expected-result result))))

(deftest explicit-chaining-bulk-test
  (let [empty-table (vec (repeat 11 '()))
        result (reduce (fn [table k] (logic.explicit-chaining/insert table k))
                       empty-table
                       explicit-chaining-test-insertions)]
    (is (= explicit-chaining-expected-result result))))

