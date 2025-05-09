(ns hash-experiment.core
  (:gen-class)
  (:require
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn make-hash-map
  [m]
  (vec (repeat  m '())))

(def test-table (make-hash-map 11))
(def test-table-2 (make-hash-map 11))

(-> test-table
    (logic.explicit-chaining/insert 27 11)
    (logic.explicit-chaining/insert 18 11)
    (logic.explicit-chaining/insert 29 11)
    (logic.explicit-chaining/insert 43 11)
    (logic.explicit-chaining/insert 77 11)
    (logic.explicit-chaining/insert 13 11)
    (logic.explicit-chaining/insert 16 11)
    (logic.explicit-chaining/insert 40 11))

(-> test-table-2
    (logic.double-hashing/insert 27 11)
    (logic.double-hashing/insert 18 11)
    (logic.double-hashing/insert 29 11)
    (logic.double-hashing/insert 43 11)
    (logic.double-hashing/insert 77 11)
    (logic.double-hashing/insert 16 11)
    (logic.double-hashing/insert 40 11)
    (logic.double-hashing/insert 49 11)
    (logic.double-hashing/insert 5 11))

