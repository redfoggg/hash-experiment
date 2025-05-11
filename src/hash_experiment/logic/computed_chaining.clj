(ns hash-experiment.logic.computed-chaining)

(defn create-table
  [m]
  (vec (repeat m {:key nil :value nil :next nil})))

(defn hashing
  [k m]
  (mod k m))

(defn hashing-2
  [k m]
  (mod (max 1 (quot k m)) m))

(defn insert
  [table k]
  (let [m (count table)
        idx (hashing k m)
        jump (hashing-2 k m)]

    (if (nil? (get-in table [idx :key]))
      (assoc-in table [idx] {:key k :value k :next nil})
      (loop [current-idx idx
             prev-idx    nil
             accesses    0]
        (cond
          (>= accesses m) (throw (Exception. "Tabela cheia!"))

          (nil? (get-in table [current-idx :key]))
          (let [new-idx       current-idx
                updated-table (assoc-in table [new-idx] {:key k :value k :next nil})]
            (if (= prev-idx nil)
              updated-table
              (assoc-in updated-table [prev-idx :next] new-idx)))

          :else
          (let [next-idx (mod (+ current-idx jump) m)]
            (recur next-idx
                   (if (= (get-in table [current-idx :next]) nil)
                     current-idx
                     prev-idx)
                   (inc accesses))))))))

(defn search
  [table k]
  (let [m (count table)
        initial-idx (hashing k m)]
    (loop [current-idx initial-idx
           accesses 0]
      (cond
        (>= accesses m) {:accesses accesses :value nil}
        (= (get-in table [current-idx :key]) k) {:accesses accesses :value (get-in table [current-idx :value])}
        :else (recur (get-in table [current-idx :next])
                     (inc accesses))))))
