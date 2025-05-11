(ns hash-experiment.logic.explicit-chaining)

(defn hashing
  [k m]
  (mod k m))

(defn insert
  [table k]
  (let [m              (count table)
        index          (hashing k m)
        bucket         (nth table index)
        updated-bucket (conj bucket k)]
    (assoc table index updated-bucket)))

(defn search
  [table k]
  (let [m      (count table)
        index  (hashing k m)
        bucket (nth table index)]
    (loop [items bucket
           accesses 1]
      (if-let [item (first items)]
        (if (= item k)
          {:accesses accesses :value item}
          (recur (rest items) (inc accesses)))
        {:accesses accesses :value nil}))))

