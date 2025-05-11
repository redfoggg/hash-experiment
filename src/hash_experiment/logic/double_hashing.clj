(ns hash-experiment.logic.double-hashing)

(defn hashing
  [k m]
  (mod k m))

(defn hashing-2
  [k m]
  (mod (max 1 (quot k m)) m))

(defn insert 
  [table k]
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

        (nil? (nth tb current-index))
        (assoc tb current-index k)

        :else
        (recur (mod (+ current-index jump) m)
               (inc attempts)
               tb)))))

(defn search
  [table k]
  (let [m            (count table)
        initial-idx  (hashing k m)
        jump         (hashing-2 k m)
        max-attempts m]
    (loop [current-index initial-idx
           attempts      1]
      (cond
        (> attempts max-attempts)  ; Percorreu toda a tabela
        nil
        
        (nil? (nth table current-index))  ; Encontrou posição vazia
        nil
        
        (= (nth table current-index) k)  ; Encontrou a chave
        k
        
        :else  ; Continua procurando
        (recur (mod (+ current-index jump) m)
               (inc attempts))))))

