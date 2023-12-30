package P08Exams.P03Exam.core;

import P08Exams.P03Exam.models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerImpl implements TaskManager {

    private Map<String, Task> tasks;
    private Map<String, Task> executedTasks;

    public TaskManagerImpl() {
        this.tasks = new LinkedHashMap<>();
        this.executedTasks = new LinkedHashMap<>();
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public boolean contains(Task task) {
        return tasks.containsKey(task.getId()) || executedTasks.containsKey(task.getId());
    }

    @Override
    public int size() {
        return tasks.size() + executedTasks.size();
    }

    @Override
    public Task getTask(String taskId) {
        if (!tasks.containsKey(taskId) && !executedTasks.containsKey(taskId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " not found in both maps.");
        }
        Task task = null;
        if (tasks.containsKey(taskId)) {
            task = tasks.get(taskId);
        }
        if (executedTasks.containsKey(taskId)) {
            task = executedTasks.get(taskId);
        }

        return task;
    }

    @Override
    public void deleteTask(String taskId) {
        if (!tasks.containsKey(taskId) && !executedTasks.containsKey(taskId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " not found in both maps.");
        }

        tasks.remove(taskId);
        executedTasks.remove(taskId);
    }

    @Override
    public Task executeTask() {
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException("No tasks to execute.");
        }

        Task task = tasks.values().iterator().next();

        this.deleteTask(task.getId());
        executedTasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void rescheduleTask(String taskId) {
        if (!executedTasks.containsKey(taskId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " not found in both maps.");
        }
        Task task = executedTasks.remove(taskId);

        tasks.put(task.getId(), task);
    }

    @Override
    public Iterable<Task> getDomainTasks(String domain) {
        List<Task> domainTasks = tasks.values().stream()
                .filter(task -> task.getDomain().equals(domain))
                .collect(Collectors.toList());

        if (domainTasks.isEmpty()) {
            throw new IllegalArgumentException("No tasks found in the specified domain.");
        }

        return domainTasks;
    }

    @Override
    public Iterable<Task> getTasksInEETRange(int lowerBound, int upperBound) {
        List<Task> tasksInRange = new ArrayList<>();
        for (Task task : tasks.values()) {
            int eet = task.getEstimatedExecutionTime();
            if (eet >= lowerBound && eet <= upperBound) {
                tasksInRange.add(task);
            }
        }
        return tasksInRange;
    }

    @Override
    public Iterable<Task> getAllTasksOrderedByEETThenByName() {
        List<Task> taskList1 = new ArrayList<>(executedTasks.values());

        List<Task> sortedTasks = new ArrayList<>(tasks.values());
        sortedTasks.addAll(taskList1);
        sortedTasks.sort(Comparator.comparing(Task::getEstimatedExecutionTime).reversed()
                .thenComparing(task -> task.getName().length()));
        return sortedTasks;
    }
}