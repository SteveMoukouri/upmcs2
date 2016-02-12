(ns embla.emblagl
  (:import org.lwjgl.glfw.GLFW
           org.lwjgl.opengl.GL
           org.lwjgl.opengl.GL11
           org.lwjgl.Version
           )
  (:require [embla.signals :as sigs])
  (:gen-class))


(def glfw-win (atom ()))
(def callback-vector (atom ()))

(defn versions
  "Get LWJGL/GLFW versions and print"
  []
  (println (str "Using GLFW v" (GLFW/GLFW_VERSION_MAJOR) "." 
                (GLFW/GLFW_VERSION_MINOR) "." (GLFW/GLFW_VERSION_REVISION)))
  (println (str "Using LWJGL v" (Version/VERSION_MAJOR) "." 
                (Version/VERSION_MINOR) "." (Version/VERSION_REVISION))))

(defn callback-unset 
  "If user kept a reference to a callback, he can unset it here"
  [callback]
  (swap! @callback-vector (fn [l] (filter (fn [c] (not= c callback)) l)))
  (.release callback))

(defn callback-register 
  "Register Java callbacks to release them on exit"
  [window callback]
  (GLFW/glfwSetKeyCallback window callback)
  (swap! callback-vector (fn [l] (cons callback l))))

(defn callback-make
  "Create a Java GLFWKeyCallback with passed key treatment function"
  [window function]
  (proxy [org.lwjgl.glfw.GLFWKeyCallback] []
    (invoke [window key scancode action mods]
      (function window key scancode action mods))))

(defn escape-to-quit
  [window key scancode action mods]
  (if (and (= key GLFW/GLFW_KEY_ESCAPE) (= action GLFW/GLFW_RELEASE))
    (GLFW/glfwSetWindowShouldClose window GLFW/GLFW_TRUE)))

(defn opengl-init
  "Initialize context etc for openGL display"
  ;; TODO: 
  ;;    - make a giant (?) callback for keyboard presses
  ;;    - make a smaller callback for emitting a time signal
  ;; Make a class for each, and use it? Or deftype?
  []
  (let [height 300 width 300 init-result (GLFW/glfwInit)]
    (if (=  init-result GLFW/GLFW_TRUE)
      (java.lang.IllegalStateException. "Unable to initialize GLFW"))
    ;; Window behaviour hints
    (GLFW/glfwDefaultWindowHints)
    (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
    (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
    ;; Window creation
    (let [window (GLFW/glfwCreateWindow width height "Hello World" 0 0)]
      (if (nil? window)
        (RuntimeException. "Unable to create the GLFW window")
        ;; Assign window value to atom if created
        (swap! glfw-win (fn [x] window)))
      ;; ESC quits the window
      (let [callback
            (callback-make @glfw-win escape-to-quit)]                        
        (GLFW/glfwSetKeyCallback @glfw-win callback))
      (let [video-mode (GLFW/glfwGetVideoMode (GLFW/glfwGetPrimaryMonitor))]
        (GLFW/glfwSetWindowPos @glfw-win
                               (/ (- (.width video-mode) width) 2) 
                               (/ (- (.height video-mode) height) 2)))
      (GLFW/glfwMakeContextCurrent @glfw-win)
      (GLFW/glfwSwapInterval 1)
      (GLFW/glfwShowWindow @glfw-win))))

(defn opengl-loop
  "OpenGL loop."
  []
  (GL/createCapabilities)
  (GL11/glClearColor 1.0 0.0 0.0 0.0)
  (while (= (GLFW/glfwWindowShouldClose @glfw-win) GLFW/GLFW_FALSE)
    (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))
    (GLFW/glfwSwapBuffers @glfw-win)
    (GLFW/glfwPollEvents))
  (GLFW/glfwDestroyWindow @glfw-win))

(defn opengl-terminate
  "Clean up after the window"
  []
  ;; Release callbacks
  (doseq [callback @callback-vector] (try (.release callback)))
  (println "Terminating...")
  (GLFW/glfwTerminate))
