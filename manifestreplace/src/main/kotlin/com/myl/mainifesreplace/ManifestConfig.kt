package com.myl.mainifesreplace

open class ManifestConfig {
    var enable: Boolean = true
    var activityCount: Int = 0
    var providerCount: Int = 0
    var serviceCount: Int = 0
    var receiverCount: Int = 0

    override fun toString(): String {
        return "ManifestConfig\n" +
                "\tenable=$enable" +
                "\tactivityCount=$activityCount" +
                "\tproviderCount=$providerCount\n" +
                "\tserviceCount=$serviceCount\n" +
                "\treceiverCount=$receiverCount\n"
    }
}