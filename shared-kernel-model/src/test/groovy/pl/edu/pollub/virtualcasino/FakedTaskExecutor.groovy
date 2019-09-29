package pl.edu.pollub.virtualcasino

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class FakedTaskExecutor {

    Map<Instant, Runnable> tasksToExecute = new ConcurrentHashMap<Instant, Runnable>()

    void addTask(Instant pointInTime, Runnable task) {
        tasksToExecute.put(pointInTime, task)
    }

    void executeTasksForTimePoint(Instant currentPointInTime) {
        def tasksToExecuteForCurrentPointInTime = tasksToExecute
                .findAll { taskExecutionPointInTime, task -> taskExecutionPointInTime <= currentPointInTime }
        tasksToExecuteForCurrentPointInTime.forEach({ taskExecutionPointInTime, task ->
            task.run()
            tasksToExecute.remove(taskExecutionPointInTime)
        })
    }

}
