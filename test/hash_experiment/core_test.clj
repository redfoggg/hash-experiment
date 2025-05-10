(ns hash-experiment.core-test
  (:require [clojure.test :refer :all]
            [hash-experiment.core :refer :all]
            [hash-experiment.logic.double-hashing :as logic.double-hashing]))

(def test-insertions
  [27 18 29 43 77 16 40 49 5])

(def expected-result
  ['(77) '() '(40) '(49) '()
   '(27) '(16) '(18) '(5)
   '(29) '(43)])

(deftest double-hashing-bulk-test
  (let [empty-table (vec (repeat 11 nil))
        result (reduce (fn [table k] (logic.double-hashing/insert table k))
                       empty-table
                       test-insertions)]
    (is (= expected-result result))))

