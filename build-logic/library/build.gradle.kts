plugins {
    `kotlin-dsl`
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
            id = "com.pubiqq.lifecycleprops.build_logic.library"
            implementationClass = "com.pubiqq.lifecycleprops.build_logic.library.LibraryPlugin"
        }
    }
}