package com.origemite.batchrealtime.job

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean

class SampleJob2 : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        println("sample2")
    }
}