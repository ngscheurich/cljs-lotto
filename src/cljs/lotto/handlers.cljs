(ns lotto.handlers
  (:require [re-frame.core :as re-frame]
            [lotto.db :as db]
            [lotto.cards :as cards]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(defn deal-cards [card-names width height]
  (let [num-cards              (* width height)
        num-pairs              (/ num-cards 2)
        shuffled-card-names    (shuffle card-names)
        chosen-card-names      (take num-pairs shuffled-card-names)
        chosen-card-name-pairs (concat chosen-card-names chosen-card-names)
        shuffled-card-names    (shuffle chosen-card-name-pairs)
        shuffled-cards         (for [card-name shuffled-card-names]
                                 (cards/card card-name :back))
        row-of-cards           (partition width shuffled-cards)
        grid-of-cards          (vec (for [row row-of-cards]
                                      (vec row)))]
    grid-of-cards))

(re-frame/reg-event-db
 :deal-cards
 (fn [db [_ card-names width height]]
   (assoc db :cards (deal-cards card-names width height)
          :current-player :A)))

(re-frame/reg-event-db
 :flip-up
 (fn [db [_ x y]]
   (update-in db [:cards y x] cards/flip-up)))
