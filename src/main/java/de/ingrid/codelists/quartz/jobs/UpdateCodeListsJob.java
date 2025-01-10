/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2025 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelists.quartz.jobs;

import de.ingrid.codelists.CodeListService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class UpdateCodeListsJob extends QuartzJobBean {
    
    private final static Logger log = LogManager.getLogger(UpdateCodeListsJob.class);

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
