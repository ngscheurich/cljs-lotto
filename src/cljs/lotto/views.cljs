(ns lotto.views
  (:require [lotto.utils :refer [state-viewer]]
            [re-frame.core :as re-frame]
            [lotto.cards :as cards]))

(defn card-face [face]
  [:div.front
   [:div.card {:style {:font-size "240px"}}
    [:span.face (get cards/mahjong face)]]])

(defn card-back []
  [:div.back
   [:div.card]])

(defn oriented-card [card]
  [:div {:class (if (cards/front? card)
                  "flip-container"
                  "flip-container back-of-card")}
   ;; [:div.flipper {:style
   ;;                {:transform
   ;;                 (str "rotate(" (if (> (rand-int 2) 0) (rand-int -4) (rand-int 4)) "deg)")}}
   [:div.flipper
    [card-back]
    [card-face (cards/face card)]]])

(defn flipper [x y]
  (let [card (re-frame/subscribe [:card-at x y])]
    [:div {:on-click (fn [] (re-frame/dispatch [:flip-up x y]))}
     [oriented-card @card]]))

(defn grid []
  (let [grid-size (re-frame/subscribe [:grid-size])
        height (get @grid-size :height)
        width  (get @grid-size :width)]
    [:table.grid
     [:tbody
      (for [y (range height)]
        [:tr
         (for [x (range width)] [:td [flipper x y]])])]]))

(defn main-panel []
  (let [current-player (re-frame/subscribe [:current-player])]
    [:div
     [grid]
     [:button
      {:on-click (fn [] (re-frame/dispatch [:deal-cards (keys cards/mahjong) 5 4]))}
      "Deal a new game"]
     [:div.app-state
      [:dl
       [:dt "Current Player"]
       [:dd @current-player]
       ]
      [state-viewer]]]))
