plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("LifecyclePropsCommonPlugin") {
            id = "com.pubiqq.lifecycleprops.build_logic.common"
            implementationClass = "com.pubiqq.lifecycleprops.build_logic.common.CommonPlugin"
        }
    }
}