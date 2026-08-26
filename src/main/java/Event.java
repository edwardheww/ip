public class Event extends Task {

    private String startDT;
    private String endDT;

    public Event(String task, String startDT, String endDT) {
        super(task);
        this.startDT = startDT;
        this.endDT = endDT;
    }

    public Event(String task, String startDT, String endDT, boolean checked) {
        super(task, checked);
        this.startDT = startDT;
        this.endDT = endDT;
    }

    public String getMemoryFormat() {
        return "E:"
                + (super.isChecked() ? "X" : " ") + ":"
                + super.getTask() + ":"
                + this.startDT + ":"
                + this.endDT;
    }

    @Override
    public String toString() {
        return "[E]"
                + super.toString()
                + " (from: " + startDT
                + " to: " + endDT
                + ")";
    }

}
