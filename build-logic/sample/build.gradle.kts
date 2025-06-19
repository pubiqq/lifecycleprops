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
        register("LifecyclePropsSamplePlugin") {
            id = "com.pubiqq.lifecycleprops.buildlogic.sample"
            implementationClass = "com.pubiqq.lifecycleprops.buildlogic.sample.SamplePlugin"
        }
    }
}