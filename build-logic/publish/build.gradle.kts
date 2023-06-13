plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.android.tools)
}

gradlePlugin {
    plugins {
        register("LifecyclePropsPublishPlugin") {
            id = "com.pubiqq.lifecycleprops.build_logic.publish"
            implementationClass = "com.pubiqq.lifecycleprops.build_logic.publish.PublishPlugin"
        }
    }
}