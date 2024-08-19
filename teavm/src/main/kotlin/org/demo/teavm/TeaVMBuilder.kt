package org.demo.teavm

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass
import java.io.File

/** Builds the TeaVM/HTML application. */
@SkipClass
object TeaVMBuilder {
    @JvmStatic fun main(arguments: Array<String>) {
        val teaBuildConfiguration = TeaBuildConfiguration().apply {
            assetsPath = arrayListOf(AssetFileHandle(File("../assets").canonicalPath))
            webappPath = File("build/dist").canonicalPath
            // Register any extra classpath assets here:
            // additionalAssetsClasspathFiles += "org/demo/asset.extension"
        }

        // Register any classes or packages that require reflection here:
        // TeaReflectionSupplier.addReflectionClass("org.demo.reflect")

        val tool = TeaBuilder.config(teaBuildConfiguration)
        tool.mainClass = "org.demo.teavm.TeaVMLauncher"
        TeaBuilder.build(tool)
    }
}
