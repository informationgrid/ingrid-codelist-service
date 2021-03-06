InGrid Codelist Service
=========

This library is part of the InGrid software package. It provides common functionality to handle codelists (fetch from repository on schedule, persist codelists, access codelists). HTTP Proxy information will be picked up automatically from JVM configurations. 

Usage
=========
This service can be configured via Spring where the following settings are needed in the application-context.xml.

\#1 Add one communication bean, whether to request Management iPlug or direct http:

    <bean id="httpCommunication" class="de.ingrid.codelists.comm.HttpCLCommunication">
        <property name="requestUrl" value="http://localhost:8082/rest/getCodelists" />
    </bean>

\#2 Add one (or more) persistency beans where a copy of the codelists shall be saved to:

    <bean id="dbPersistency" class="de.ingrid.codelists.persistency.DbCodeListPersistency"></bean>

and/or:

    <bean id="xmlPersistency" class="de.ingrid.codelists.persistency.XmlCodeListPersistency">
        <property name="pathToXml" value="data/codelists.xml" />    
    </bean>

and/or:

    <bean id="igePersistency" class="de.ingrid.codelists.persistency.IgeCodeListPersistency">
        <property name="catalogService"><ref bean="catalogService" /></property>    
    </bean>

\#3 Initialize the Service

    <bean id="codeListService" class="de.ingrid.codelists.CodeListService">
        <property name="comm" ref="httpCommunication" />
        <property name="persistencies">
            <list>
                <ref bean="xmlPersistency" />
                <ref bean="igePersistency" />
            </list>
        </property>
        <property name="defaultPersistency" value="0" />
    </bean>

Moreover there's the possibility to configure a Quartz-task that requests the Codelist-Repository at a configured time:

    <bean id="scheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
        <property name="triggers">
            <list>
                <ref bean="simpleTrigger" />
                <ref bean="simpleTriggerCodeLists" />
            </list>
        </property>
        <property name="applicationContextSchedulerContextKey">  
            <value>applicationContext</value>  
        </property> 
    </bean>

    <bean name="updateCodeListsJobDetail" class="org.springframework.scheduling.quartz.JobDetailBean">
        <property name="jobClass" value="de.ingrid.codelists.quartz.jobs.UpdateCodeListsJob" />
    </bean>

    <bean id="simpleTriggerCodeLists" class="org.springframework.scheduling.quartz.SimpleTriggerBean">
        <property name="jobDetail" ref="updateCodeListsJobDetail" />
        <property name="startDelay" value="0" />
        <property name="repeatInterval" value="600000" />
    </bean>

Contribute
----------

- Issue Tracker: https://github.com/informationgrid/ingrid-codelist-service/issues
- Source Code: https://github.com/informationgrid/codelist-service
 
### Set up eclipse project

```
mvn eclipse:eclipse
```

and import project into eclipse.

Support
-------

If you are having issues, please let us know: info@informationgrid.eu

License
-------

The project is licensed under the EUPL license.
