package com.pubiqq.lifecycleprops.sample

import android.app.Application

class Application : Application() {

//    override fun onCreate() {
//        super.onCreate()
//
//        @OptIn(ExperimentalConfigurationApi::class)
//        with(LifecycleProps) {
//            // Sets default configurations for lifecycle-aware properties
//            setDefaultLifecycleAwareConfigurations(
//                readOnlyPropsConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                readWritePropsConfiguration = LifecycleAwareReadWriteConfiguration.Default()
//            )
//        }
//
//        @OptIn(ExperimentalConfigurationApi::class)
//        with(LifecyclePropsAndroid) {
//            // Sets default configurations for Android-specific lifecycle-aware properties
//            setDefaultViewLifecycleAwareConfigurations(
//                readOnlyPropsConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                readWritePropsConfiguration = LifecycleAwareReadWriteConfiguration.Default()
//            )
//        }
//    }
}
