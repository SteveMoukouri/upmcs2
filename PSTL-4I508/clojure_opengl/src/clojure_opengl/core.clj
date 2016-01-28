(ns clojure-opengl.core
  (:import org.lwjgl.glfw.GLFW
           org.lwjgl.Version)
  (:gen-class))

(defn versions
  "Get LWJGL/GLFW versions"
  []
  (println (str "Using GLFW v" (GLFW/GLFW_VERSION_MAJOR) "." (GLFW/GLFW_VERSION_MINOR) "." (GLFW/GLFW_VERSION_REVISION)))
  (println (str "Using LWJGL v" (Version/VERSION_MAJOR) "." (Version/VERSION_MINOR) "." (Version/VERSION_REVISION)))
  )

(defn emptyWindow
  "Create an empty OpenGL window. Coooool."
  []
  (versions)
  (GLFW/glfwInit)
  (def w (Integer. 300))
  (def s (String. "Hello"))
  (let [window (GLFW/glfwCreateWindow w w s nil nil)]
    (if (nil? window) false (GLFW/glfwShowWindow window)
        )
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (emptyWindow)
  )
