package walle.task;

/**
 * A task with a description only, no associated date/time.
 */
public class ToDo extends Task {

    /**
     * Creates a new, not-yet-done ToDo with the given description.
     *
     * @param task the task's description.
     */
    public ToDo(String task) {
        super(task);
    }

    /**
     * Creates a ToDo with the given description and completion status, e.g.
     * when restoring a task previously loaded from storage.
     *
     * @param task    the task's description.
     * @param checked whether the task is already marked done.
     */
    public ToDo(String task, boolean checked) {
        super(task, checked);
    }

    /**
     * Returns this ToDo's line format for the memory file: {@code T;<checkmark>;<task>}.
     *
     * @return the task's line format for the memory file.
     */
    public String getMemoryFormat() {
        return "T;"
                + (super.isChecked() ? "X" : " ") + ";"
                + super.getTask();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
