(ns hash-experiment.core
  (:gen-class)
  (:require
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]))

(def test-table (logic.explicit-chaining/create-table 11))
(def test-table-2 (logic.double-hashing/create-table 11))
(def test-table-3 (logic.computed-chaining/create-table 11))

(def explicit-chaining-table
  (-> test-table
      (logic.explicit-chaining/insert 27 11)
      (logic.explicit-chaining/insert 18 11)
      (logic.explicit-chaining/insert 29 11)
      (logic.explicit-chaining/insert 43 11)
      (logic.explicit-chaining/insert 77 11)
      (logic.explicit-chaining/insert 13 11)
      (logic.explicit-chaining/insert 16 11)
      (logic.explicit-chaining/insert 40 11)))
(logic.explicit-chaining/search explicit-chaining-table 5 11)
(logic.explicit-chaining/search explicit-chaining-table 27 11)

(def double-hashing-table
  (-> test-table-2
      (logic.double-hashing/insert 27 11)
      (logic.double-hashing/insert 18 11)
      (logic.double-hashing/insert 29 11)
      (logic.double-hashing/insert 43 11)
      (logic.double-hashing/insert 77 11)
      (logic.double-hashing/insert 16 11)
      (logic.double-hashing/insert 40 11)
      (logic.double-hashing/insert 49 11)
      (logic.double-hashing/insert 5  11)))
(logic.double-hashing/search double-hashing-table 4 11)
(logic.double-hashing/search double-hashing-table 5 11)
double-hashing-table
(logic.double-hashing/hashing 5 11)
(logic.double-hashing/hashing-2 5 11)

(logic.double-hashing/search double-hashing-table 18 11)
(logic.double-hashing/search double-hashing-table 29 11)
(logic.double-hashing/search double-hashing-table 40 11)

(def computed-chaining-table (-> test-table-3
                                 (logic.computed-chaining/insert 27 7)
                                 (logic.computed-chaining/insert 18 7)
                                 (logic.computed-chaining/insert 29 7)
                                 (logic.computed-chaining/insert 28 7)
                                 (logic.computed-chaining/insert 39 7)
                                 (logic.computed-chaining/insert 13 7)
                                 (logic.computed-chaining/insert 16 7)))
computed-chaining-table
(logic.computed-chaining/search computed-chaining-table 3 7)
(logic.computed-chaining/search computed-chaining-table 29 7)
(logic.computed-chaining/search computed-chaining-table 28 7)

