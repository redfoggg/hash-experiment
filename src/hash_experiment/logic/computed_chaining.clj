(ns hash-experiment.logic.computed-chaining)

(defn create-table
  [m]
  (vec (repeat m nil)))

(defn hashing
  [k m]
  (mod k m))

(defn hashing-2
  [k m]
  (mod (quot k m) m))

(defn- home-location?
  [k m index]
  (= (hashing k m) index))

(defn insert
  [table k m]
  (let [initial-index (hashing k m)]
    (loop [index             initial-index
           attempts          0
           current-table     table
           current-increment nil]
      (let [elem (when (not (>= index m)) (nth current-table index))]
        (cond
          (>= index m)
          current-table

          (= (:value elem) k)
          current-table

          (>= attempts m)
          current-table

          (nil? elem)
          (assoc current-table index {:value k :next nil})

          (not (nil? current-increment))
          (recur (+ current-increment index) (inc attempts) current-table current-increment)

          (home-location? (:value elem) m index)
          (let [increment (hashing-2 (:value elem) m)]
            (recur (+ increment index)
                   (inc attempts)
                   (update current-table index #(assoc % :next increment))
                   increment))
          :else current-table)))))

(defn search
  [table k m]
  (let [initial-index (hashing k m)]
    (loop [index initial-index
           increment nil
           elem      (nth table initial-index)
           accesses 1]
      (cond
        (>= accesses m)
        {:accesses accesses :value nil}

        (nil? elem)
        {:accesses accesses :value nil}

        (= (:value elem) k)
        {:accesses accesses :value k}

        (not (nil? increment))
        (let [new-increment (+ index increment)]
          (recur new-increment new-increment (nth table new-increment) (inc accesses)))

        :else
        (let [increment (+ index (:next elem))
              new-elem  (nth table increment)]
          (recur increment increment new-elem (inc accesses)))))))
