plugins {
    `kotlin-dsl`
}

dependencies {
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
        register("LifecyclePropsCommonPlugin") {
            id = "com.pubiqq.lifecycleprops.buildlogic.common"
            implementationClass = "com.pubiqq.lifecycleprops.buildlogic.common.CommonPlugin"
        }
    }
}