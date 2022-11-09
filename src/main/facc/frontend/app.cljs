(ns facc.frontend.app
  (:require [reagent.dom :as rdom]))

(comment
  (def pow js/Math.pow)
  
  (defn reciprocal [f] #(/ 1 (f %1 %2)))
  
  (defn present-value [i n] (pow (/ 1 (+ 1 i)) n))
  
  (defn geometric-series-present-value [i g n]
    (cond
      (neg-int? n) (/ 1 (- i g))
      (= i g) (/ n (+ i g))
      :else (/ (- 1 (pow (/ (+ 1 g) (+ 1 i)) n)) (- i g))))
  
  (rdom/render [:div "hello space"] (js/document.getElementById "app")))

(defn init []
  (println "Hello World")
  (rdom/render [:div "hello world"] (js/document.getElementById "app")))