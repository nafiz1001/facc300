(ns facc.frontend.app
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]))

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

(def tvm-equivalents {:P {:F present-value
                          :A series-present-value}
                      :F {:P compound-amount
                          :A series-compound-amount}
                      :A {:P capital-recovery
                          :F sinking-fund}})

(defn tvm-compute [i n target others]
  (let [equivalents (tvm-equivalents target)]
    (->> others
         (map #(let [[key val] %] (* val ((equivalents key) i n))))
         (apply +)
         (-))))

(defn newton-method [f f']
  (let [epsilon 1e-8
        max-iteration 1000
        aux (fn [i iteration]
              (if (> (js/Math.abs (f i)) epsilon)
                (if (zero? iteration)
                  js/NaN
                  (let [di (js/Math.min (/ (f i) (f' i)) (/ iteration max-iteration))
                        next-x (js/Math.max (- i di) 0)]
                    (recur next-x (dec iteration))))
                (* (js/Math.ceil (/ i epsilon)) epsilon)))]
    (aux 1 max-iteration)))

(defn tvm-compute-interest [n pv pmt fv]
  (letfn [(solution [i] (+ (* pv (compound-amount i n))
                           (* pmt (series-compound-amount i n))
                           fv))
          (gradient [i] (let [compound-gradient (* n (compound-amount i (dec n)))]
                          (+ (* pv compound-gradient)
                              ;;  (* pmt (->> (range 1 (inc n)) (map compound-gradient) (apply +)))
                             (* pmt (/ (- compound-gradient (series-compound-amount i n)) i)))))]
    (newton-method solution gradient)))

(defn atom-input [value]
  [:input {:type "number"
           :value @value
           :on-change #(reset! value (-> % .-target .-value js/parseFloat))}])

(defn tvm-calculator []
  (let [N (r/atom 0)
        I (r/atom 0)
        PV (r/atom 0)
        PMT (r/atom 0)
        FV (r/atom 0)

        compute-i (fn [] (reset! I (tvm-compute-interest @N @PV @PMT @FV)))
        compute-pv (fn [] (reset! PV (tvm-compute @I @N :P {:F @FV :PMT @PMT})))
        compute-pmt (fn [] (reset! PMT (tvm-compute @I @N :PMT {:P @PV :F @FV})))
        compute-fv (fn [] (reset! FV (tvm-compute @I @N :F {:P @PV :PMT @PMT})))]
    (fn []
      [:div
       [:div "N" [atom-input N]]
       [:div "I/Y" [atom-input I]
        [:input {:type "button" :value "CPT I" :on-click compute-i}]]
       [:div "PV" [atom-input PV]
        [:input {:type "button" :value "CPT PV" :on-click compute-pv}]]
       [:div "PMT" [atom-input PMT]
        [:input {:type "button" :value "CPT PMT" :on-click compute-pmt}]]
       [:div "FV" [atom-input FV]
        [:input {:type "button" :value "CPT FV" :on-click compute-fv}]]])))

(defn init []
  (rdom/render [tvm-calculator] (js/document.getElementById "app")))