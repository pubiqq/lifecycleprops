package com.pubiqq.lifecycleprops.sample

import android.app.Application

class Application : Application() {

//    override fun onCreate() {
//        super.onCreate()
//
//        @OptIn(LifecycleAwareConfigurationApi::class)
//        with(LifecycleProps) {
//            // Sets default configurations for lifecycle-aware properties
//            setLifecycleAwareConfigurations(
//                readOnlyPropsConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                readWritePropsConfiguration = LifecycleAwareReadWriteConfiguration.Default()
//            )
//        }
//
//        @OptIn(LifecycleAwareConfigurationApi::class)
//        with(LifecyclePropsAndroid) {
//            // Sets default configurations for android-specific lifecycle-aware properties
//            setViewLifecycleAwareConfigurations(
//                readOnlyPropsConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                readWritePropsConfiguration = LifecycleAwareReadWriteConfiguration.Default()
//            )
//        }
//    }
}