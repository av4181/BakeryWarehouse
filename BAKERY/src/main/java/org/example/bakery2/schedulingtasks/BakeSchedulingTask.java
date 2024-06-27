package org.example.bakery2.schedulingtasks;

import org.example.bakery2.services.BakingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class BakeSchedulingTask {

    private static final Logger logger = LoggerFactory.getLogger(BakeSchedulingTask.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final BakingService bakingService;

    public BakeSchedulingTask(BakingService bakingService) {
        this.bakingService = bakingService;
    }

    //second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "0 0 22 * * *")
    public void bakeAllTheGoodsEverydayAtGivenTime() {
        logger.info("It's time to start baking all the goods!");
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        bakingService.bakeAllProducts();
    }
}
