(ns hash-experiment.logic.double-hashing)

(defn hashing
  [k m]
  (mod k m))

(defn hashing-2
  [k m]
  (mod (max 1 (quot k m)) m))

(defn insert [table k]
  (let [m            (count table)
        index        (hashing k m)
        jump         (hashing-2 k m)
        max-attempts m]
    (loop [current-index index
           attempts      1
           tb            table]
      (cond
        (> attempts max-attempts)
        (throw (Exception. "Tabela hash cheia"))

        (empty? (nth tb current-index))
        (assoc tb current-index (conj (nth tb current-index) k))

        :else
        (recur (mod (+ current-index jump) m)
               (inc attempts)
               tb)))))

