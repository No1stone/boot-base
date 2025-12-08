package com.origemite.batchrealtime.job

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class SampleJob1 : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        println("sample1")
    }
}