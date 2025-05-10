(ns hash-experiment.logic.computed-chaining)

(defn create-table
  "Cria uma tabela vazia com m slots"
  [m]
  (vec (repeat m {:key nil :value nil :next nil})))

(defn primary-hash
  "Função hash primária"
  [k m]
  (mod k m))

(defn secondary-hash
  "Função hash secundária (nunca retorna zero)"
  [k m]
  (inc (mod k (dec m))))

(defn insert
  "Insere um par chave-valor na tabela usando computed chaining"
  [table k v]
  (let [m (count table)
        idx (primary-hash k m)
        jump (secondary-hash k m)]

    (if (nil? (get-in table [idx :key]))
      (assoc-in table [idx] {:key k :value v :next nil})
      (loop [current-idx idx
             prev-idx nil
             attempts 0]
        (cond
          (>= attempts m) (throw (Exception. "Tabela cheia!"))

          (nil? (get-in table [current-idx :key]))
          (let [new-idx current-idx
                updated-table (assoc-in table [new-idx] {:key k :value v :next nil})]
            (if (= prev-idx nil)
              updated-table
              (assoc-in updated-table [prev-idx :next] new-idx)))

          :else
          (let [next-idx (mod (+ current-idx jump) m)]
            (recur next-idx
                   (if (= (get-in table [current-idx :next]) nil)
                     current-idx
                     prev-idx)
                   (inc attempts))))))))

(defn search
  "Busca um valor na tabela pela chave"
  [table k]
  (let [m (count table)
        initial-idx (primary-hash k m)]
    (loop [current-idx initial-idx
           attempts 0]
      (cond
        (>= attempts m) nil
        (= (get-in table [current-idx :key]) k) (get-in table [current-idx :value])
        :else (recur (get-in table [current-idx :next])
                     (inc attempts))))))
