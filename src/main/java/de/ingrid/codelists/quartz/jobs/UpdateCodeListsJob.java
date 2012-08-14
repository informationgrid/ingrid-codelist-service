package de.ingrid.codelists.quartz.jobs;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import de.ingrid.codelists.CodeListService;

public class UpdateCodeListsJob extends QuartzJobBean {
    
    private final static Logger log = Logger.getLogger(UpdateCodeListsJob.class);

    public UpdateCodeListsJob() {}
    
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        if (log.isDebugEnabled()) {
            log.debug("Executing UpdateCodeListsJob...");
        }
        
        CodeListService clService = getClServiceFromBean(jobExecutionContext);
        // if null is returned then this means that an error occured!
        Object modifiedCodelists = clService.updateFromServer(clService.getLastModifiedTimestamp());
        
        if (log.isDebugEnabled()) {
            log.debug("UpdateCodeListsJob finished! (successful = " + (modifiedCodelists == null ? false : true) + ")");
        }
    }

    public CodeListService getClServiceFromBean(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schedulerContext = null;  
        try {  
            schedulerContext = jobExecutionContext.getScheduler().getContext();  
        } catch(SchedulerException e) {  
            throw new JobExecutionException("Failure accessing scheduler context", e);  
        } 
        ApplicationContext appContext = (ApplicationContext)schedulerContext.get("applicationContext");  
        //String jobBeanName = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("codeListService");
        
        return (CodeListService) appContext.getBean("codeListService");
    }

}
