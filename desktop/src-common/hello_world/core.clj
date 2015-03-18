(ns hello-world.core
	(:require
		[play-clj.core :refer :all]
		[play-clj.ui :refer :all]
		[play-clj.g2d :refer :all]))

(defn move [entity direction]
	(case direction
		:up (assoc entity :y (+ 10 (:y entity)))
		:down (assoc entity :y (+ -10 (:y entity)))
		:right (assoc entity :x (+ 10 (:x entity)))
		:left (assoc entity :x (+ -10 (:x entity)))))

(defscreen main-screen
	:on-show
	(fn [screen entities]
		(add-timer! screen :spawn-enemy 2 2 19)
		(update! screen :renderer (stage) :camera (orthographic))
		(assoc (texture "Clojure_logo.gif")
			:x 50 :y 50 :width 50 :height 50
			:angle 45 :origin-x 0 :origin-y 0))

	:on-render
	(fn [screen entities]
		(clear!)
		(render! screen entities))

	:on-resize
	(fn [screen entities]
		(height! screen 600))

	:on-timer
	(fn [screen entities]
		(case (:id screen)
			:spawn-enemy (println "Create a spawn enemy fn!")#_(conj entities (create-enemy))
			nil))

	:on-key-down
	(fn [screen entities]
		(cond
			(= (:key screen) (key-code :dpad-up))
				(move (first entities) :up)
			(= (:key screen) (key-code :dpad-down))
				(move (first entities) :down)
			(= (:key screen) (key-code :dpad-right))
				(move (first entities) :right)
			(= (:key screen) (key-code :dpad-left))
				(move (first entities) :left)))

	:on-touch-down
	(fn [screen entities]
		(cond
			(> (game :y) (* (game :height) (/ 2 3)))
				(move (first entities) :up)
			(< (game :y) (/ (game :height) 3))
				(move (first entities) :down)
			(> (game :x) (* (game :width) (/ 2 3)))
				(move (first entities) :right)
			(< (game :x) (/ (game :width) 3))
				(move (first entities) :left)))
		)

(defgame hello-world
	:on-create
	(fn [this]
		(set-screen! this main-screen)))
