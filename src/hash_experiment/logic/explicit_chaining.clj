(ns hash-experiment.logic.explicit-chaining)

(defn hashing
  [k m]
  (mod k m))

(defn insert 
  [table k]
  (let [m              (count table)
        index          (hashing k m)
        bucket         (nth table index)
        updated-bucket (conj bucket (list k))]
    (assoc table index updated-bucket)))

