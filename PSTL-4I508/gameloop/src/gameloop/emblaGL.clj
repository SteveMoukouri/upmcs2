(ns clojure-opengl.emblagl
  (:import org.lwjgl.glfw.GLFW
           org.lwjgl.opengl.GL
           org.lwjgl.opengl.GL11
           org.lwjgl.Version
           )
  (:gen-class))

(defn versions
  "Get LWJGL/GLFW versions"
  []
  (println (str "Using GLFW v" (GLFW/GLFW_VERSION_MAJOR) "." (GLFW/GLFW_VERSION_MINOR) "." (GLFW/GLFW_VERSION_REVISION)))
  (println (str "Using LWJGL v" (Version/VERSION_MAJOR) "." (Version/VERSION_MINOR) "." (Version/VERSION_REVISION)))
  )

(def opengl-window (atom ()))

(defn opengl-init
  "Initialize context etc for openGL display"
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
        (swap! opengl-window (fn [x] window)))
      (let [video-mode (GLFW/glfwGetVideoMode (GLFW/glfwGetPrimaryMonitor))]
        (GLFW/glfwSetWindowPos (deref opengl-window)
                               (/ (- (.width video-mode) width) 2) 
                               (/ (- (.height video-mode) height) 2)))
      (GLFW/glfwMakeContextCurrent (deref opengl-window))
      (GLFW/glfwSwapInterval 1)
      (GLFW/glfwShowWindow (deref opengl-window)))))

(defn opengl-loop
  "OpenGL loop."
  []
  (GL/createCapabilities)
  (GL11/glClearColor 1.0 0.0 0.0 0.0)
  (while (= (GLFW/glfwWindowShouldClose (deref opengl-window)) GLFW/GLFW_FALSE)
    (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))
    (GLFW/glfwSwapBuffers (deref opengl-window))
    (GLFW/glfwPollEvents)))
