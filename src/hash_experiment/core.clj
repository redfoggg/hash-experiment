(ns hash-experiment.core
  (:gen-class)
  (:require
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]))

(def test-table (vec (repeat 11 '())))
(def test-table-2 (vec (repeat 11 nil)))
(def test-table-3 (logic.computed-chaining/create-table 11))

(def explicit-chaining-table
  (-> test-table
      (logic.explicit-chaining/insert 27)
      (logic.explicit-chaining/insert 18)
      (logic.explicit-chaining/insert 29)
      (logic.explicit-chaining/insert 43)
      (logic.explicit-chaining/insert 77)
      (logic.explicit-chaining/insert 13)
      (logic.explicit-chaining/insert 16)
      (logic.explicit-chaining/insert 40)))
(logic.explicit-chaining/search explicit-chaining-table 5)
(logic.explicit-chaining/search explicit-chaining-table 27)

(def double-hashing-table
  (-> test-table-2
      (logic.double-hashing/insert 27)
      (logic.double-hashing/insert 18)
      (logic.double-hashing/insert 29)
      (logic.double-hashing/insert 43)
      (logic.double-hashing/insert 77)
      (logic.double-hashing/insert 16)
      (logic.double-hashing/insert 40)
      (logic.double-hashing/insert 49)
      (logic.double-hashing/insert 5)))
(logic.double-hashing/search double-hashing-table 4)
(logic.double-hashing/search double-hashing-table 40)

(def computed-chaining-table (-> test-table-3
                                 (logic.computed-chaining/insert 27)
                                 (logic.computed-chaining/insert 18)
                                 (logic.computed-chaining/insert 29)
                                 (logic.computed-chaining/insert 43)
                                 (logic.computed-chaining/insert 77)
                                 (logic.computed-chaining/insert 16)
                                 (logic.computed-chaining/insert 40)
                                 (logic.computed-chaining/insert 49)
                                 (logic.computed-chaining/insert 5)))
(logic.computed-chaining/search computed-chaining-table 3)
(logic.computed-chaining/search computed-chaining-table 5)

