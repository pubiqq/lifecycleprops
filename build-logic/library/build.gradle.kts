plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("LifecyclePropsLibraryPlugin") {
            id = "com.pubiqq.lifecycleprops.build_logic.library"
            implementationClass = "com.pubiqq.lifecycleprops.build_logic.library.LibraryPlugin"
        }
    }
}