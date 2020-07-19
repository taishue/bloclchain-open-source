package com.coinsthai.task;

import com.coinsthai.model.Withdraw;
import com.coinsthai.pojo.intenum.WithdrawStatus;
import com.coinsthai.repository.WithdrawRepository;
import com.coinsthai.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author 
 */
@Component
public class WithdrawProcessTask {

    @Autowired
    private WithdrawRepository repository;

    @Value("${app.schedule.withdraw.process.delay}")
    private int delayProcess = 0;

    // 每分钟执行一次
    @Scheduled(cron = "0 * * * * ?")
    public void processWithdraws() {
        Date date = DateUtils.addMinute(new Date(), 0 - delayProcess);
        List<Withdraw> list = repository.findByStatusAndCreatedAtLessThan(WithdrawStatus.PENDING, date);
        if (!list.isEmpty()) {
            list.forEach(model -> model.setStatus(WithdrawStatus.PROCESSING));
            repository.save(list);
        }
    }
}
