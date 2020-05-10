package com.kalvin.kvf.modules.workflow.config;


import com.kalvin.kvf.modules.workflow.event.GlobalActivityEventListener;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * Create by Kalvin on 2020/4/20.
 */
@Configuration
public class ActivityConfig {

    @Resource
    private DataSource dataSource;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @Resource
    private GlobalActivityEventListener globalActivityEventListener;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
        SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
        spec.setDataSource(dataSource);
        spec.setTransactionManager(platformTransactionManager);
//        Resource[] resources = null;

        // 是否自动创建流程引擎表
        spec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        spec.setAsyncExecutorActivate(false);
        // 流程历史等级
        spec.setHistoryLevel(HistoryLevel.FULL);

        // 添加全局监听事件
        ArrayList<ActivitiEventListener> activityEventListeners = new ArrayList<>();
        activityEventListeners.add(globalActivityEventListener);
        spec.setEventListeners(activityEventListeners);

        // 解决流程图汉字乱码问题
        spec.setActivityFontName("宋体");
        spec.setLabelFontName("宋体");
        spec.setAnnotationFontName("宋体");

        return spec;
    }

    /*@Bean
    public ProcessEngineFactoryBean processEngine() {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return processEngineFactoryBean;
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }*/

}
