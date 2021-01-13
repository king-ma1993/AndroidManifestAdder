package com.myl.mainifesreplace

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.myl.mainifesreplace.AppConstant.MANIFEST_CONFIG
import com.myl.mainifesreplace.AppConstant.TAG
import com.myl.mainifesreplace.manifest.ComponentsGenerator
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class ManifestPlugin : Plugin<Project> {
    var mProject: Project? = null

    override fun apply(project: Project) {
        mProject = project
        project.extensions.create(MANIFEST_CONFIG, ManifestConfig::class.java)

        val appPlugin = project.plugins.getPlugin(AppPlugin::class.java)
        if (appPlugin != null) {
            val android = project.extensions.getByType(AppExtension::class.java)
            android.applicationVariants.all { it ->
                val config = project.extensions.getByName(MANIFEST_CONFIG) as ManifestConfig
                println("$TAG ManifestPlugin:${config}!")
                if (!config.enable) {
                    println("$TAG ManifestPlugin disable!")
                    return@all
                }
                val appId = it.generateBuildConfig.appPackageName
                val newManifest = ComponentsGenerator.generateComponent(appId, config)
                it.outputs.forEach {
                    val manifestOutFile = it.processManifest.manifestOutputDirectory
                    it.processManifest.doLast {
                        updateManifest(manifestOutFile, newManifest as String?)
                    }
                }
            }

        }
    }

    private fun updateManifest(file: File?, newManifest: String?) {
        // 除了目录和AndroidManifest.xml之外，还可能会包含manifest-merger-debug-report.txt等不相干的文件，过滤它
        if (file == null || !file.exists() || newManifest == null) return
        if (file.isDirectory) {
            println("$TAG updateManifest: $file")
            file.listFiles().forEach {
                updateManifest(it, newManifest)
            }
        } else if (file.name.equals("AndroidManifest.xml", ignoreCase = true)) {
            appendManifest(file, newManifest)
        }
    }

    private fun appendManifest(file: File, newManifest: String) {
        if (file == null || !file.exists()) return
        println("$TAG appendManifest: $file")
        val updatedContent =
            file.readText(charset = Charsets.UTF_8)
                .replace("</application>", "$newManifest</application>")

        file.writeText(updatedContent, charset = Charsets.UTF_8)
    }


}