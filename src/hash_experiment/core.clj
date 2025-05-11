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
explicit-chaining-table ; => [(77) () (13) () () (16 27) () (40 29 18) () () (43)]
(logic.explicit-chaining/search explicit-chaining-table 5) ; => nil
(logic.explicit-chaining/search explicit-chaining-table 27) ; => 27

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
double-hashing-table ; => [77 nil 40 49 nil 27 16 18 5 29 43]
(logic.double-hashing/search double-hashing-table 4) ; => nil
(logic.double-hashing/search double-hashing-table 40) ; => 40

(def computed-chaining-table (-> test-table-3
                                 (logic.computed-chaining/insert 27 "valor27")
                                 (logic.computed-chaining/insert 18 "valor18")
                                 (logic.computed-chaining/insert 29 "valor29")
                                 (logic.computed-chaining/insert 43 "valor43")
                                 (logic.computed-chaining/insert 77 "valor77")
                                 (logic.computed-chaining/insert 16 "valor16")
                                 (logic.computed-chaining/insert 40 "valor40")
                                 (logic.computed-chaining/insert 49 "valor49")
                                 (logic.computed-chaining/insert 5 "valor5")))
computed-chaining-table ; => [{:key 77, :value "valor77", :next nil} {:key 16, :value "valor16", :next 2} {:key 5, :value "valor5", :next nil} {:key nil, :value nil, :next nil} {:key 49, :value "valor49", :next nil} {:key 27, :value "valor27", :next 1} {:key 29, :value "valor29", :next nil} {:key 18, :value "valor18", :next 6} {:key 40, :value "valor40", :next nil} {:key nil, :value nil, :next nil} {:key 43, :value "valor43", :next nil}]
(logic.computed-chaining/search computed-chaining-table 3) ; => nil
(logic.computed-chaining/search computed-chaining-table 77) ; => "valor77"

