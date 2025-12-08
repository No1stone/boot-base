package com.origemite.batchrealtime.config

import org.quartz.Scheduler
import org.quartz.TriggerListener
import org.quartz.impl.QuartzServer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class QuartzConfig {

    private var scheduler: Scheduler? = null
    private var quartzServer: QuartzServer? = null
    private var JobRepository: JobRepository? = null
    private var PlatformTransactionManager : PlatformTransactionManager? = null
    private var triggerListener: TriggerListener? = null



}