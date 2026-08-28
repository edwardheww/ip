import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    private LocalDateTime startDT;
    private LocalDateTime endDT;

    public Event(String task, LocalDateTime startDT, LocalDateTime endDT) {
        super(task);
        this.startDT = startDT;
        this.endDT = endDT;
    }

    public Event(String task, LocalDateTime startDT, LocalDateTime endDT, boolean checked) {
        super(task, checked);
        this.startDT = startDT;
        this.endDT = endDT;
    }

    public String getMemoryFormat() {
        return "E;"
                + (super.isChecked() ? "X" : " ") + ";"
                + super.getTask() + ";"
                + this.startDT + ";"
                + this.endDT;
    }

    @Override
    public String toString() {
        return "[E]"
                + super.toString()
                + " (from: " + this.startDT.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " to: " + this.endDT.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + ")";
    }

}
