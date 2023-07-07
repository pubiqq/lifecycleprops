package com.pubiqq.lifecycleprops.sample

import android.app.Application

class Application : Application() {

//    override fun onCreate() {
//        super.onCreate()
//
//        @OptIn(LifecycleAwareConfigurationApi::class)
//        with(LifecycleProps) {
//            // Set default configurations for lifecycle-aware properties (coarse-grained)
//            setDefaultConfigurations(
//                readOnlyConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                readWriteConfiguration = LifecycleAwareReadWriteConfiguration.Default()
//            )
//
//            // Set default configurations for lifecycle-aware properties (fine-grained)
//            setDefaultConfigurations(
//                lifecycleAwareReadOnlyConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                lifecycleAwareReadWriteConfiguration = LifecycleAwareReadWriteConfiguration.Default(),
//                viewLifecycleAwareReadOnlyConfiguration = LifecycleAwareReadOnlyConfiguration.Default(),
//                viewLifecycleAwareReadWriteConfiguration = LifecycleAwareReadWriteConfiguration.Default()
//            )
//        }
//    }
}