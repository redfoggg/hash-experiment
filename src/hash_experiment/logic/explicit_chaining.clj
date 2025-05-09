(ns hash-experiment.logic.explicit-chaining)

(defn hashing
  [k m]
  (mod k m))

(defn insert 
  [hash-map k m]
  (let [index (hashing k m)
        bucket (nth hash-map index)
        updated-bucket (conj bucket (list k))]
    (assoc hash-map index updated-bucket)))

