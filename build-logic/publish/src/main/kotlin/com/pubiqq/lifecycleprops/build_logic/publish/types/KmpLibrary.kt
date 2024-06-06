package com.pubiqq.lifecycleprops.build_logic.publish.types

import com.android.build.api.variant.AndroidComponentsExtension
import com.pubiqq.lifecycleprops.build_logic.publish.PublishConfig
import com.pubiqq.lifecycleprops.build_logic.publish.PublishExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

internal fun Project.configureKmpLibraryPublication(extension: PublishExtension) {
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()

    if (plugins.hasPlugin("com.android.library")) {
        plugins.withId("com.android.library") {
            the(AndroidComponentsExtension::class).finalizeDsl {
                val config = loadPublishConfig(project, extension)
                configureKmpLibraryPublication(config)
            }
        }
    } else {
        afterEvaluate {
            val config = loadPublishConfig(project, extension)
            configureKmpLibraryPublication(config)
        }
    }
}

private fun Project.configureKmpLibraryPublication(config: PublishConfig) {
    configurePublishing(config)
    configureRepositories(config)
    configureSigning(config)
}

private fun Project.configurePublishing(config: PublishConfig) {
    configure<KotlinMultiplatformExtension> {
        withSourcesJar()

        if (targets.any { it is KotlinAndroidTarget }) {
            androidTarget {
                publishLibraryVariants = config.publishVariants
            }
        }
    }

    // `artifactId` for the Android target is set after `finalizeDsl()`, so we explicitly defer the maven publication
    // configuration.
    afterEvaluate {
        configure<PublishingExtension> {
            publications.withType<MavenPublication>().configureEach {
                groupId = config.groupId
                version = config.version
                artifactId = getArtifactIdForMultiplatform(project.name, config.artifactId)

                pom(config.pom)
            }
        }
    }
}

// XXX: https://youtrack.jetbrains.com/issue/KT-50001/MPP-allow-to-specify-custom-artifact-base-name
private fun MavenPublication.getArtifactIdForMultiplatform(
    projectName: String,
    artifactId: String
): String {
    return this.artifactId.replaceFirst(projectName, artifactId)
}