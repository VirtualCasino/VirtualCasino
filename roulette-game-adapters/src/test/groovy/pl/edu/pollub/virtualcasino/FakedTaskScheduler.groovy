package pl.edu.pollub.virtualcasino

import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

import java.time.Instant
import java.util.concurrent.ScheduledFuture

class FakedTaskScheduler extends ThreadPoolTaskScheduler implements TaskScheduler {

    private final FakedTaskExecutor taskExecutor

    FakedTaskScheduler(FakedTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor
    }

    @Override
    ScheduledFuture<?> schedule(Runnable task, Instant startTime) {
        return taskExecutor.addTask(startTime, task)
    }

}