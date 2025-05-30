(ns hash-experiment.logic.double-hashing)

(defn create-table
  [m]
  (vec (repeat m nil)))

(defn hashing
  [k m]
  (mod k m))

(defn hashing-2
  [k m]
  (mod (max 1 (quot k m)) m))

(defn insert
  [table k m]
  (let [index        (hashing k m)
        jump         (hashing-2 k m)
        max-attempts m]
    (loop [current-index index
           accesses      1
           current-table table]
      (cond
        (>= current-index m)
        current-table

        (> accesses max-attempts)
        current-table

        (nil? (nth current-table current-index))
        (assoc current-table current-index k)

        :else
        (recur (mod (+ current-index jump) m)
               (inc accesses)
               current-table)))))

(defn search
  [table k m]
  (let [initial-idx  (hashing k m)
        jump         (hashing-2 k m)
        max-attempts m]
    (loop [current-index initial-idx
           accesses      1]
      (cond
        (> accesses max-attempts)
        {:accesses accesses :value nil}

        (nil? (nth table current-index))
        {:accesses accesses :value nil}

        (= (nth table current-index) k)
        {:accesses accesses :value k}

        :else
        (recur (mod (+ current-index jump) m)
               (inc accesses))))))

