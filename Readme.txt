- Quartz library is only needed when using Quartz Job! Otherwise exclude it from the dependency!


- Changes for applicationContext file for bean configuration:

---------------------------------------------------------------------
- neccessary changes
---------------------------------------------------------------------
- add one communication bean, whether to request Management iPlug or direct http
    <bean id="httpCommunication" class="de.ingrid.codelists.comm.HttpCLCommunication">
        <property name="requestUrl" value="http://localhost:8082/rest/getCodelists" />
    </bean>
    
- add one or more persistency beans where a copy of the codelists shall be saved to
    <bean id="dbPersistency" class="de.ingrid.codelists.persistency.DbCodeListPersistency"></bean>
    <bean id="xmlPersistency" class="de.ingrid.codelists.persistency.XmlCodeListPersistency">
        <property name="pathToXml" value="data/codelists.xml" />    
    </bean>
    <bean id="igePersistency" class="de.ingrid.codelists.persistency.IgeCodeListPersistency">
        <property name="catalogService"><ref bean="catalogService" /></property>    
    </bean>
    
- initialize the Service
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
    
---------------------------------------------------------------------
- if using Quartz Job to automatically update codelists
---------------------------------------------------------------------
- update SchedulerFactoryBean    
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
    
