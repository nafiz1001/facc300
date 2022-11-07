(ns facc.frontend.app
  (:require [reagent.dom :as rdom]))

(comment 
  (defn tvm-table []
    (let [reciprocal (fn [f] #(/ 1 (f %1 %2)))
          compound-amount (fn [i, n] (js/Math.pow (+ 1 i) n))
          present-value (reciprocal compound-amount)
          series-compound-amount (fn [i, n] (/ (- (compound-amount i n) 1) i))
          sinking-fund (reciprocal series-compound-amount)
          series-present-value (fn [i, n] (if (neg-int? n)
                                            (/ 1 i)
                                            (/ (- 1 (present-value i n)) i)))
          capital-recovery (reciprocal series-present-value)
          uniform-gradient-series (fn [i, n] (- (/ 1 i) (/ n (- (compound-amount i n) 1))))
          ])
  )
)

(defn init []
  (println "Hello World")
  (rdom/render [:div "hello world"] (js/document.getElementById "app")))