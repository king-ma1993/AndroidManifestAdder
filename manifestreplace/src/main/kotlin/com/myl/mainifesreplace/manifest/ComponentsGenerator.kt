package com.myl.mainifesreplace.manifest

import com.myl.mainifesreplace.ManifestConfig
import org.redundent.kotlin.xml.xml


object ComponentsGenerator {

    private const val ac_infix = "a.Activity"
    private const val ac_name = "android:name"

    private const val pd_infix = "Provider"
    private const val sc_infix = "s.Service"
    private const val rc_infix = "s.Receiver"
    private const val authorities = "android:authorities"
    private const val pd_author = "pdauthor"
    private const val exported = "android:exported"
    private const val intentFilter = "intent-filter"
    private const val action = "action"


    fun generateComponent(applicationID: String, manifestConfig: ManifestConfig): String {
        val application = xml("application") {
            for (i in 1..manifestConfig.activityCount) {
                "activity" {
                    attribute(ac_name, "${applicationID}.${ac_infix}AC$i")
                }
            }

            for (i in 1..manifestConfig.providerCount) {
                "provider" {
                    attribute(ac_name, "${applicationID}.${pd_infix}PD$i")
                    attribute(authorities, "${applicationID}.$pd_author$i")
                    attribute(exported, "false")
                }
            }

            for (i in 1..manifestConfig.serviceCount) {
                "service" {
                    attribute(ac_name, "${applicationID}.${sc_infix}SC$i")
                }
            }

            for (i in 1..manifestConfig.receiverCount) {
                "receiver" {
                    attribute(ac_name, "${applicationID}.${rc_infix}RC$i")
                    attribute(exported, "false")
                    intentFilter {
                        action {
                            attribute(ac_name, "${applicationID}.${action}RC$i")
                        }
                    }
                }
            }

        }
        return application.toString().replace("<application>", "").replace("</application>", "")
    }
}