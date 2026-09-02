package walle.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A task with a description and a due date/time.
 */
public class Deadline extends Task {

    private LocalDateTime endDt;

    /**
     * Creates a new, not-yet-done Deadline with the given description and due date/time.
     *
     * @param task  the task's description.
     * @param endDt the date/time the task is due by.
     */
    public Deadline(String task, LocalDateTime endDt) {
        super(task);
        this.endDt = endDt;
    }

    /**
     * Creates a Deadline with the given description, due date/time, and completion
     * status, e.g. when restoring a task previously loaded from storage.
     *
     * @param task    the task's description.
     * @param endDt   the date/time the task is due by.
     * @param checked whether the task is already marked done.
     */
    public Deadline(String task, LocalDateTime endDt, boolean checked) {
        super(task, checked);
        this.endDt = endDt;
    }

    /**
     * Returns this Deadline's line format for the memory file: {@code D;<checkmark>;<task>;<endDt>}.
     *
     * @return the task's line format for the memory file.
     */
    public String getMemoryFormat() {
        return "D;"
                + (super.isChecked() ? "X" : " ") + ";"
                + super.getTask() + ";"
                + this.endDt;
    }

    @Override
    public String toString() {
        return "[D]"
                + super.toString()
                + " (by: " + this.endDt.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }

}
