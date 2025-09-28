package com.github.eliadoarias.tgb.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SendJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String userId = jobExecutionContext.getJobDetail().getKey().getName();
    }
}
