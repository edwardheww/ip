package walle.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A task with a description and a start/end date-time range.
 */
public class Event extends Task {

    private LocalDateTime startDt;
    private LocalDateTime endDt;

    /**
     * Creates a new, not-yet-done Event with the given description and time range.
     *
     * @param task    the task's description.
     * @param startDt the date/time the event starts.
     * @param endDt   the date/time the event ends.
     */
    public Event(String task, LocalDateTime startDt, LocalDateTime endDt) {
        super(task);
        this.startDt = startDt;
        this.endDt = endDt;
    }

    /**
     * Creates an Event with the given description, time range, and completion
     * status, e.g. when restoring a task previously loaded from storage.
     *
     * @param task      the task's description.
     * @param startDt   the date/time the event starts.
     * @param endDt     the date/time the event ends.
     * @param isChecked whether the task is already marked done.
     */
    public Event(String task, LocalDateTime startDt, LocalDateTime endDt, boolean isChecked) {
        super(task, isChecked);
        this.startDt = startDt;
        this.endDt = endDt;
    }

    /**
     * Returns this Event's line format for the memory file: {@code E;<checkmark>;<task>;<startDt>;<endDt>}.
     *
     * @return the task's line format for the memory file.
     */
    public String getMemoryFormat() {
        return "E;"
                + (super.isChecked() ? "X" : " ") + ";"
                + super.getTask() + ";"
                + this.startDt + ";"
                + this.endDt;
    }

    @Override
    public String toString() {
        return "[E]"
                + super.toString()
                + " (from: " + this.startDt.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " to: " + this.endDt.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + ")";
    }

}
