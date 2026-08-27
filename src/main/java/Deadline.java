public class Deadline extends Task {

    private String endDT;

    public Deadline(String task, String endDate) {
        super(task);
        this.endDT = endDate;
    }

    public Deadline(String task, String endDate, boolean checked) {
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
