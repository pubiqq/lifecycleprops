plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("LifecyclePropsPublishPlugin") {
            id = "com.pubiqq.lifecycleprops.build_logic.publish"
            implementationClass = "com.pubiqq.lifecycleprops.build_logic.publish.PublishPlugin"
        }
    }
}