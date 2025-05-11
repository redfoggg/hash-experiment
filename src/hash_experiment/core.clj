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
(logic.double-hashing/search double-hashing-table 5)
(logic.double-hashing/search double-hashing-table 18)
(logic.double-hashing/search double-hashing-table 29)
(logic.double-hashing/search double-hashing-table 40)

(def computed-chaining-table (-> test-table-3
                                 (logic.computed-chaining/insert 27)
                                 (logic.computed-chaining/insert 18)
                                 (logic.computed-chaining/insert 29)
                                 (logic.computed-chaining/insert 28)
                                 (logic.computed-chaining/insert 39)
                                 (logic.computed-chaining/insert 13)
                                 (logic.computed-chaining/insert 16)))
computed-chaining-table
(logic.computed-chaining/search computed-chaining-table 3)
(logic.computed-chaining/search computed-chaining-table 29)
(logic.computed-chaining/search computed-chaining-table 28)

