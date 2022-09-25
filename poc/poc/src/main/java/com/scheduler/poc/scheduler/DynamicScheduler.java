package com.scheduler.poc.scheduler;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.scheduler.poc.entity.ConfigItem;
import com.scheduler.poc.repository.ConfigRepo;
@EnableScheduling
@Service
public class DynamicScheduler implements SchedulingConfigurer {
	private static Logger LOGGER = LoggerFactory.getLogger(DynamicScheduler.class);
	 @Autowired
	 ConfigRepo repo;
	 
	// @PostConstruct
	    public void initDatabase() {
	        ConfigItem config = new ConfigItem("next_exec_time", "30");
	        repo.save(config);
	    }
	    
	    @Bean
	    public TaskScheduler poolScheduler() {
	        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
	        scheduler.setPoolSize(1);
	        scheduler.initialize();
	        return scheduler;
	    }
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		 taskRegistrar.setScheduler(poolScheduler());
		 
		 taskRegistrar.addTriggerTask(() -> scheduleCron(repo.findById("next_exec_time1").get().getConfigValue()), t -> {
	            CronTrigger crontrigger = new CronTrigger(repo.findById("next_exec_time1").get().getConfigValue());
	            return crontrigger.nextExecutionTime(t);
	        });

	}
	 public void scheduleCron(String cron) {
	    	Format f = new SimpleDateFormat("hh:mm:ss a");
	    	String strResult = f.format(new Date());
	    	 LOGGER.info(strResult);
	        LOGGER.info("scheduleCron: Next execution time of this taken from cron expression -> {}", cron);
	    }
}
