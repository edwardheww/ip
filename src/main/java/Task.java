public class Task {

    private String task;
    private boolean checked;

    public Task(String task) {
        this.task = task;
        this.checked = false;
    }

    public void check() {
        this.checked = true;
    }

    public void uncheck() {
        this.checked = false;
    }

    private String getCheckbox() {
        return this.checked ? "[X]" : "[ ]";
    }

    @Override
    public String toString() {
        return this.getCheckbox() + " " + this.task;
    }

}
