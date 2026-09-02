package walle.task;

import java.util.ArrayList;

import walle.storage.Storage;
import walle.ui.Ui;

/**
 * Holds the list of tasks WALLE is tracking, and provides operations to
 * add, remove, mark, and retrieve tasks by their 1-based position in the list.
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list wrapping tasks already loaded (e.g. from {@link Storage}).
     *
     * @param tasks the tasks to start with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at the given 1-based position.
     *
     * @param pos the 1-based position of the task to remove.
     * @return the task that was removed.
     */
    public Task delete(int pos) {
        return this.tasks.remove(pos - 1);
    }

    /**
     * Returns the task at the given 1-based position.
     *
     * @param pos the 1-based position of the task.
     * @return the task at that position.
     */
    public Task get(int pos) {
        return this.tasks.get(pos - 1);
    }

    /**
     * Marks the task at the given 1-based position as done.
     *
     * @param pos the 1-based position of the task to mark.
     * @return the task that was marked.
     */
    public Task mark(int pos) {
        Task task = this.tasks.get(pos - 1);
        task.check();
        return task;
    }

    /**
     * Marks the task at the given 1-based position as not done.
     *
     * @param pos the 1-based position of the task to unmark.
     * @return the task that was unmarked.
     */
    public Task unmark(int pos) {
        Task task = this.tasks.get(pos - 1);
        task.uncheck();
        return task;
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the number of tasks.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Returns the underlying list of tasks, e.g. for {@link Storage#save} or
     * {@link Ui#listTasks}.
     *
     * @return the underlying list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

}
