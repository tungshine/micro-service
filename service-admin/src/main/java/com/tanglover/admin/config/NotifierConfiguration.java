package com.tanglover.admin.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.Notifier;
import de.codecentric.boot.admin.server.notify.RemindingNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author TangXu
 * @version V1.0
 * @Title: 为监控的服务添加邮件通知
 * @date 2017年6月14日 上午10:18:13
 */

//@Configuration
//@EnableScheduling
public class NotifierConfiguration {

    @Autowired
    private Notifier notifier;

    //服务上线或者下线都通知
    private String[] reminderStatuses = {"DOWN"};

    private InstanceRepository repository;

    @Bean
    @Primary
    public RemindingNotifier remindingNotifier() {
        RemindingNotifier remindingNotifier = new RemindingNotifier(notifier, repository);
        //设定时间，5分钟提醒一次
//        remindingNotifier.setReminderPeriod(TimeUnit.MINUTES.toMillis(5));
        //设定监控服务状态，状态改变为给定值的时候提醒
        remindingNotifier.setReminderStatuses(reminderStatuses);

        repository.findByName("service-A").filter(instance -> send(instance));
        return remindingNotifier;
    }

    protected boolean send(Instance instance) {
        StatusInfo statusInfo = instance.getStatusInfo();
        InstanceId id = instance.getId();
        System.out.println("statusInfo:" + statusInfo + " --- InstanceId:" + id);
        return false;
    }

    @Scheduled(fixedRate = 60_000L)
    public void remind() {
        remindingNotifier().setReminderStatuses(reminderStatuses);
    }
}