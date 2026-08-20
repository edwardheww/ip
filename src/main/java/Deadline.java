public class Deadline extends Task {

    private String endDT;

    public Deadline(String task, String endDate) {
        super(task);
        this.endDT = endDate;
    }

    @Override
    public String toString() {
        return "[D]"
                + super.toString()
                + " (by: " + this.endDT + ")";
    }

}
