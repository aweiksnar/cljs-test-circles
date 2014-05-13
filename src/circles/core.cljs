(ns circles.core)

(enable-console-print!)

(defn largest-key [map]
  "largest key in a map with integers for keys ex: {2 :two 1 :one} ;=> 2"
  (apply max (keys map)))

(defn set-svg-attr [el attr value]
  (.setAttribute el attr value))

(defn append [el parent]
  (.appendChild parent el))

(defn el-by-id [id]
  (.getElementById js/document id))

(defn set-html [el & html]
  (set! (.-innerHTML el) (apply str html)))

(def sheet (el-by-id "sheet"))

(defn sheet-rect [e]
  ;; hardcoded to sheet b/c firefox svg weirdness
  (.getBoundingClientRect sheet))

(defn x-mouse-pos [e]
  (- (.-pageX e) (.-left (sheet-rect e)) (.-scrollX js/window)))

(defn y-mouse-pos [e]
  (- (.-pageY e) (.-top (sheet-rect e)) (.-scrollY js/window)))

(def rad-input (el-by-id "circle-radius"))

(defn rad-input-val []
  (js/parseInt (.-value rad-input)))

(defn circle-color []
  (.-value (.querySelector js/document "input[name='color']:checked")))

(defn create-svg [parent id width height]
  (set-html parent
     (str "<?xml version=1.0'?>
           <svg id="id" width='"width"' height='"height"' version='1.1'
             xmlns='http://www.w3.org/2000/svg'>
           </svg>")))

(create-svg sheet "my-svg" 600 400)

(def my-svg (el-by-id "my-svg"))

(def marks (atom {}))

(def mark-counter (atom 0))

(defn add-circle [x y rad id container]
  (let [circle (.createElementNS js/document "http://www.w3.org/2000/svg" "circle")]
    (set-svg-attr circle "r" rad)
    (set-svg-attr circle "cx" x)
    (set-svg-attr circle "cy" y)
    (set-svg-attr circle "fill" (circle-color))
    (set-svg-attr circle "id" (str "mark-"id))
    (append circle container)))

(defn append-latest-mark
  [_ _ _ new]
  ;;[key id old new]
  (let [new-id (largest-key new)
        mrk (get @marks new-id)
        {:keys [x y]} mrk]
    (add-circle x y (rad-input-val) new-id my-svg)))

(add-watch marks :append-latest-mark append-latest-mark)

(.addEventListener rad-input "input"
  (fn [e]
    (set-html (el-by-id "curr-circle-size") (rad-input-val))))

(.addEventListener my-svg "mousedown"
  (fn [e]
    (let [el-id (.-id (.-target e))]
      (cond
        (= el-id "my-svg") (swap! marks assoc (swap! mark-counter inc)
                                                {:x (x-mouse-pos e)
                                                 :y (y-mouse-pos e)})
        :else (println "clicked on:" el-id)))))

(println "core.cljs loaded")

(set! (.-marks js/window) @marks)

;; todo:
;; cross browser layer mouse coords function (ff)
;; move/select/delete/change-size circles
;; get the changes/difference between marks atom changes
