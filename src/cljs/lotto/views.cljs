(ns lotto.views
  (:require [lotto.utils :refer [state-viewer]]
            [re-frame.core :as re-frame]
            [lotto.cards :as cards]))

(defn card-face [face]
  [:div.front
   [:div.card {:style {:font-size "240px"}}
    (get cards/mahjong face)]])

(defn card-back []
  [:div.back
   [:div.card]])

(defn oriented-card [face]
  [:div.flip-container
   [:div.flipper
    [card-back]
    [card-face face]]])

(defn main-panel []
  (let [current-player (re-frame/subscribe [:current-player])]
       [:div
        [oriented-card :one-of-circles]
        [:div.app-state
          [:dl
          [:dt "Current Player"]
          [:dd @current-player]
          ]
          [state-viewer]]]))
