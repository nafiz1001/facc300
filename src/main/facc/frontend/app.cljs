(ns facc.frontend.app
  (:require [reagent.dom :as rdom]))

(comment
  (def pow js/Math.pow)
  
  (defn reciprocal [f] #(/ 1 (f %1 %2)))
  
  (defn present-value [i n] (pow (/ 1 (+ 1 i)) n))
  (def compound-amount (reciprocal present-value))
  
  (defn geometric-series-present-value [i g n]
    (cond
      (infinite? n) (/ 1 (- i g))
      (= i g) (/ n (+ i g))
      :else (/ (- 1 (pow (/ (+ 1 g) (+ 1 i)) n)) (- i g)))) 
  
  (defprotocol Monetary-Flow
    (at [n] "get monetary flow at period n"))
  
  (defrecord Geometric-Series [N i Io g]
    Monetary-Flow
    (at [n] (if (or (<= n N) (infinite? N)) (* Io (compound-amount g n)) 0))
    (npv [n] (* Io (if (<= n N) (- (geometric-series-present-value i g N) (geometric-series-present-value i g n)) 0))))
  
  (rdom/render [:div "hello space"] (js/document.getElementById "app"))
  )

(defn init []
  (println "Hello World")
  (rdom/render [:div "hello world"] (js/document.getElementById "app")))