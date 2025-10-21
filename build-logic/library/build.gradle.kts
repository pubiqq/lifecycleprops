plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(projects.buildLogic.common)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("LifecyclePropsLibraryPlugin") {
            id = "com.pubiqq.lifecycleprops.buildlogic.library"
            implementationClass = "com.pubiqq.lifecycleprops.buildlogic.library.LibraryPlugin"
        }
    }
}
