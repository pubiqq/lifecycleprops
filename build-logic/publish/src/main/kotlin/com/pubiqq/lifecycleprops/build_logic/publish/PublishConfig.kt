package com.pubiqq.lifecycleprops.build_logic.publish

import org.gradle.api.publish.maven.MavenPom

internal class PublishConfig(
    val groupId: String,
    val version: String,
    val artifactId: String,
    val pom: (MavenPom.() -> Unit)?,
    val publishVariants: List<String>,
    val mavenRepoName: String,
    val mavenRepoUrl: String,
    val mavenRepoUserName: String?,
    val mavenRepoPassword: String?,
    val sign: Boolean,
    val signingKeyId: String?,
    val signingPassword: String?,
    val signingSecretKeyRingFile: String?
)