import java.time.LocalDateTime;

public class Deadline extends Task {

    private LocalDateTime endDT;

    public Deadline(String task, LocalDateTime endDate) {
        super(task);
        this.endDT = endDate;
    }

    public Deadline(String task, LocalDateTime endDate, boolean checked) {
        super(task, checked);
        this.endDT = endDate;
    }

    public String getMemoryFormat() {
        return "D:"
                + (super.isChecked() ? "X" : " ") + ":"
                + super.getTask() + ":"
                + this.endDT;
    }

    @Override
    public String toString() {
        return "[D]"
                + super.toString()
                + " (by: " + this.endDT + ")";
    }

}
