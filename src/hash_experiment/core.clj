(ns hash-experiment.core
  (:gen-class)
  (:require
   [clojure.set :as set]
   [hash-experiment.logic.computed-chaining :as logic.computed-chaining]
   [hash-experiment.logic.double-hashing :as logic.double-hashing]
   [hash-experiment.logic.explicit-chaining :as logic.explicit-chaining]
   [clojure.pprint :refer [pprint]]))

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

(def computed-chaining-table
  (reduce (fn [table key]
            (logic.computed-chaining/insert table key 7))
          test-table-3
          [27 18 29 28 39 13 16]))
computed-chaining-table
(logic.computed-chaining/search computed-chaining-table 3 7)
(logic.computed-chaining/search computed-chaining-table 29 7)
(logic.computed-chaining/search computed-chaining-table 28 7)
(logic.computed-chaining/search computed-chaining-table 27 7)

(defn unique-random-numbers [n]
  (let [a-set (set (take n (repeatedly #(rand-int n))))]
    (concat a-set (set/difference (set (take n (range)))
                                  a-set))))

(defn search-accesses [table keys m]
  (map (fn [key]
         (let [result (logic.computed-chaining/search table key m)]
           (:accesses result)))
       keys))

(def computed-chaining-table-2
  (let [table (logic.computed-chaining/create-table 997)
        keys (vec (take 997 (unique-random-numbers 50000)))]
    (loop [remaining-keys keys
           current-table table]
      (if (empty? remaining-keys)
        current-table
        (let [key (first remaining-keys)
              result (logic.computed-chaining/insert current-table key 997)]
          (recur (rest remaining-keys) result))))))

computed-chaining-table-2
(filter #(not (nil? %)) computed-chaining-table-2)

(def computed-accesses
  (let [keys (vec (take 997 (unique-random-numbers 50000)))
        table computed-chaining-table-2]
    (search-accesses table keys 997)))

computed-accesses


