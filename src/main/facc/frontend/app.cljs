(ns facc.frontend.app
  (:require [reagent.dom :as rdom]))

(comment
  (def pow js/Math.pow)
  
  (defn reciprocal [f] #(/ 1 (f %1 %2)))
  
  (defn compound-amount [i n] (pow (+ 1 i) n))
  (def present-value (reciprocal compound-amount))
  
  (defn series-compound-amount [i n]
    (/ (- (compound-amount i n) 1) i))
  (def sinking-fund (reciprocal series-compound-amount))
  
  (defn series-present-value [i n]
    (if (infinite? n)
      (/ 1 i)
      (/ (- 1 (present-value i n)) i)))
  (def capital-recovery (reciprocal series-present-value))
  
  (defn uniform-gradient-series [i n]
    (- (/ 1 i) (/ n (- (compound-amount i n) 1))))
  (defn geometric-series-present-value [i g n]
    (cond
      (infinite? n) (/ 1 (- i g))
      (= i g) (/ n (+ i g))
      :else (/ (- 1 (pow (/ (+ 1 g) (+ 1 i)) n)) (- i g))))
  
  (rdom/render [:div "hello space"] (js/document.getElementById "app"))
  )

(defn init []
  (println "Hello World")
  (rdom/render [:div "hello world"] (js/document.getElementById "app")))