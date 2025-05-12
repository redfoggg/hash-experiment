(ns hash-experiment.logic.perfect-hash
  (:require [incanter.core :as ic]
            [incanter.charts :as charts]
            [incanter.stats :as stats]))

(def n 1000)
(def p 10007)

(defn generate-unique-keys
  [n]
  (loop [s #{}]
    (if (= (count s) n)
      (vec s)
      (recur (conj s (rand-int 10000))))))

(defn generate-universal-params
  [p]
  {:a (inc (rand-int (dec p)))
   :b (rand-int p)})

(defn universal-hash
  [k {:keys [a b p m]}]
  (mod (mod (+ (* a k) b) p) m))

(defn second-level-hash
  [k {:keys [a b p m]}]
  (let [h (universal-hash k {:a a :b b :p p :m 1000})]
    (inc (mod h (dec m)))))

(defn create-first-level-table
  [m]
  (vec (repeat m nil)))

(defn insert-keys-first-level
  [keys hash-params]
  (let [m (:m hash-params)
        table (create-first-level-table m)]
    (reduce (fn [tbl k]
              (let [h (universal-hash k hash-params)
                    slot (get tbl h)
                    updated-slot (if slot (conj slot k) [k])]
                (assoc tbl h updated-slot)))
            table
            keys)))

(defn calculate-secondary-table-size 
  [keys]
  (let [nj (count keys)]
    (if (zero? nj)
      1
      (* nj nj))))

(defn find-collision-free-params
  [keys table-size max-attempts]
  (loop [attempt 0]
    (if (>= attempt max-attempts)
      nil
      (let [params (assoc (generate-universal-params p)
                          :p p
                          :m table-size)
            positions (map #(second-level-hash % params) keys)
            unique-positions (set positions)]
        (if (= (count unique-positions) (count keys))
          params
          (recur (inc attempt)))))))

(defn create-perfect-hash-table
  [keys]
  (let [first-level-size (* 4 (count keys))
        first-level-params (assoc (generate-universal-params p)
                                  :p p
                                  :m first-level-size)
        first-level-slots (insert-keys-first-level keys first-level-params)

        second-level-tables (mapv (fn [slot-keys]
                                    (if (nil? slot-keys)
                                      nil
                                      (let [slot-size (calculate-secondary-table-size slot-keys)
                                            params (find-collision-free-params slot-keys slot-size 100)]
                                        (if params
                                          {:params params
                                           :keys slot-keys
                                           :size slot-size}
                                          nil))))
                                  first-level-slots)]

    {:first-level-params first-level-params
     :first-level-size first-level-size
     :second-level-tables second-level-tables}))

(create-perfect-hash-table (generate-unique-keys 10))

(defn calculate-total-slots [perfect-hash-table]
  (let [first-level-size (:first-level-size perfect-hash-table)
        second-level-tables (:second-level-tables perfect-hash-table)
        second-level-sizes (map #(if % (:size %) 0) second-level-tables)]
    (+ first-level-size (reduce + second-level-sizes))))

(defn run-experiment 
  []
  (let [keys (generate-unique-keys n)
        perfect-hash-table (create-perfect-hash-table keys)
        total-slots (calculate-total-slots perfect-hash-table)]
    {:total-slots total-slots
     :ratio (/ total-slots n)}))

(defn run-experiments 
  [num-experiments]
  (let [results (repeatedly num-experiments run-experiment)
        total-slots (map :total-slots results)
        ratios (map :ratio results)

        count-less-than-2n (count (filter #(< % (* 2 n)) total-slots))
        count-less-than-4n (count (filter #(< % (* 4 n)) total-slots))

        prob-less-than-2n (/ count-less-than-2n num-experiments)
        prob-less-than-4n (/ count-less-than-4n num-experiments)]
    (println "Resultados dos Experimentos de Hashing Perfeito:")
    (println "------------------------------------------------")
    (println (str "Número de chaves (n): " n))
    (println (str "Número de experimentos: " num-experiments))
    (println)
    (println "Estatísticas sobre o número total de slots:")
    (println (str "  Mínimo: " (apply min total-slots)))
    (println (str "  Máximo: " (apply max total-slots)))
    (println (str "  Média: " (stats/mean total-slots)))
    (println (str "  Mediana: " (stats/median total-slots)))
    (println)
    (println "Verificação das afirmações teóricas:")
    (println (str "  Probabilidade de slots totais < 2n: " prob-less-than-2n))
    (println (str "  Probabilidade de slots totais < 4n: " prob-less-than-4n))

    ;; Plota o histograma
    (doto (charts/histogram ratios
                            :nbins 20
                            :title "Distribuição da Razão (Total de Slots / n)"
                            :x-label "Total de Slots / n"
                            :y-label "Frequência")
      (ic/view))

    ;; Retorna os resultados para uso posterior, se necessário
    {:total-slots-stats {:min (apply min total-slots)
                         :max (apply max total-slots)
                         :mean (stats/mean total-slots)
                         :median (stats/median total-slots)}
     :theory-verification {:prob-less-than-2n prob-less-than-2n
                           :prob-less-than-4n prob-less-than-4n}
     :raw-data {:total-slots total-slots
                :ratios ratios}}))

(run-experiments 100)
