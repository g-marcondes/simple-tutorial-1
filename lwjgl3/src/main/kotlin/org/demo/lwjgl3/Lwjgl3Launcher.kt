@file:JvmName("Lwjgl3Launcher")

package org.demo.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.demo.Drop
import org.demo.Main

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return
    Lwjgl3Application(Drop(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("drop")
        setWindowedMode(800, 480)
        useVsync(true)
        setForegroundFPS(60)
    })
}
