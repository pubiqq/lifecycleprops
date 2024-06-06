package com.pubiqq.lifecycleprops.build_logic.publish.types

import com.pubiqq.lifecycleprops.build_logic.publish.PublishConfig
import com.pubiqq.lifecycleprops.build_logic.publish.PublishExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.plugins.signing.SigningExtension
import java.util.*

internal fun loadPublishConfig(
    project: Project,
    extension: PublishExtension
): PublishConfig {
    val propsFile = project.rootProject.file("local.properties")
    if (!propsFile.exists()) {
        throw GradleException("local.properties is not found.")
    }

    val props = Properties()
    propsFile.inputStream().use { props.load(it) }

    val isSnapshot = extension.version.endsWith("-SNAPSHOT")

    return PublishConfig(
        groupId = extension.groupId,
        version = extension.version,
        artifactId = extension.artifactId,
        pom = extension.pom,
        publishVariants = listOf("release"),
        mavenRepoName = "mavenCentral",
        mavenRepoUrl = if (isSnapshot) {
            props["mavenCentral.snapshotRepoUrl"]!!.toString()
        } else {
            props["mavenCentral.releaseRepoUrl"]!!.toString()
        },
        mavenRepoUserName = props["mavenCentral.username"]?.toString(),
        mavenRepoPassword = props["mavenCentral.password"]?.toString(),
        sign = !isSnapshot,
        signingKeyId = props["signing.keyId"]?.toString(),
        signingPassword = props["signing.password"]?.toString(),
        signingSecretKeyRingFile = props["signing.secretKeyRingFile"]?.toString()
    )
}

internal fun Project.configureSigning(config: PublishConfig) {
    if (config.sign) {
        extra["signing.keyId"] = config.signingKeyId
        extra["signing.password"] = config.signingPassword
        extra["signing.secretKeyRingFile"] = config.signingSecretKeyRingFile

        configure<PublishingExtension> {
            configure<SigningExtension> {
                sign(publications)
            }
        }
    }
}

internal fun Project.configureRepositories(config: PublishConfig) {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = config.mavenRepoName
                url = uri(config.mavenRepoUrl)
                credentials {
                    username = config.mavenRepoUserName
                    password = config.mavenRepoPassword
                }
            }
        }
    }
}