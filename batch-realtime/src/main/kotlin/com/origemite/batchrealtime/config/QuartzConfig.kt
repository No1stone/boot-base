package com.origemite.batchrealtime.config

import com.origemite.batchrealtime.job.SampleJob1
import com.origemite.batchrealtime.job.SampleJob2
import jakarta.annotation.PostConstruct
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.quartz.TriggerListener
import org.quartz.impl.QuartzServer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class QuartzConfig (private val scheduler: Scheduler) {

    @PostConstruct
    fun init() {
        scheduler.clear()
        Sample1()
        Sample2()
    }

    fun Sample1(){
        val jobDetail = JobBuilder.newJob(SampleJob1::class.java)
            .withIdentity("sample1QuartzJob","sample1QuartzJob")
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("sample1QuartzJobTrigger", "sample1QuartzJobTrigger")
            .forJob(jobDetail)
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0/30 * * * * ?")
            )
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    fun Sample2(){
        val jobDetail = JobBuilder.newJob(SampleJob2::class.java)
            .withIdentity("sample2QuartzJob", "sample2QuartzJob")
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("sample2QuartzJobTrigger", "sample2QuartzJobTrigger")
            .forJob(jobDetail)
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0/30 * * * * ?")
            )
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

}