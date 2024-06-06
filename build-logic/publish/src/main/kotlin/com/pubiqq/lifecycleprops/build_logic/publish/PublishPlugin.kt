package com.pubiqq.lifecycleprops.build_logic.publish

import com.pubiqq.lifecycleprops.build_logic.publish.types.configureAndroidLibraryPublication
import com.pubiqq.lifecycleprops.build_logic.publish.types.configureKmpLibraryPublication
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            val extension = extensions.create(
                "libraryPublishing",
                PublishExtension::class.java,
                this
            )

            if (pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                configureKmpLibraryPublication(extension)
            } else if (pluginManager.hasPlugin("com.android.library")) {
                configureAndroidLibraryPublication(extension)
            } else {
                throw GradleException("Plugin is applicable only to " +
                        "\"org.jetbrains.kotlin.multiplatform\" " +
                        "and " +
                        "\"com.android.library\" projects (${target.name})")
            }
        }
    }
}