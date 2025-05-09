(ns hash-experiment.logic.double-hashing)

(defn hashing
  [k m]
  (mod k m))

(defn hashing-2
  [k m]
  (mod (max 1 (quot k m)) m))

(defn insert [hash-map k m]
  (let [index          (hashing k m)
        bucket         (nth hash-map index)
        jump           (hashing-2 k m)]
    (if (not (seq bucket))
      (assoc hash-map index (conj bucket (list k)))
      (assoc hash-map
             (+ index jump)
             (conj (nth hash-map (+ index jump)) (list k)))))) ; TODO: Double hashing tem que ser testado até que haja espaço

(hashing 16 11)
(hashing-2 16 11)

(max 1 (quot 16 11))

