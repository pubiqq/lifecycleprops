package com.pubiqq.lifecycleprops.build_logic.publish

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

open class PublishExtension(
    private val project: Project
) {
    var groupId: String = ""
    var artifactId: String = ""
    var version: String = ""

    var pom: (MavenPom.() -> Unit)? = null
        private set

    fun pom(pom: MavenPom.() -> Unit) {
        this.pom = pom
    }
}