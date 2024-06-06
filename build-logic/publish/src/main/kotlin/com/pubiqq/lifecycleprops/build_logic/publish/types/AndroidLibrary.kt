package com.pubiqq.lifecycleprops.build_logic.publish.types

import com.android.build.gradle.LibraryExtension
import com.pubiqq.lifecycleprops.build_logic.publish.PublishConfig
import com.pubiqq.lifecycleprops.build_logic.publish.PublishExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.SigningPlugin

internal fun Project.configureAndroidLibraryPublication(extension: PublishExtension) {
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()

    afterEvaluate {
        val config = loadPublishConfig(project, extension)
        configureAndroidLibraryPublication(config)
    }
}

private fun Project.configureAndroidLibraryPublication(config: PublishConfig) {
    configurePublishing(config)
    configureRepositories(config)
    configureSigning(config)
}

private fun Project.configurePublishing(config: PublishConfig) {
    require(config.publishVariants.size == 1) {
        "Only single variant publishing is supported"
    }

    val publishVariantName = config.publishVariants[0]
    configure<LibraryExtension> {
        publishing {
            singleVariant(publishVariantName) {
                withSourcesJar()
            }
        }
    }

    configure<PublishingExtension> {
        publications {
            register<MavenPublication>(publishVariantName) {
                groupId = config.groupId
                artifactId = config.artifactId
                version = config.version

                // Requires afterEvaluate, but we're already inside it
                from(components[publishVariantName])

                pom(config.pom)
            }
        }
    }
}