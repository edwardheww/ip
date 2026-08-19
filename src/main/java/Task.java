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

    @Override
    public String toString() {
        return (this.checked ? "[X]" : "[ ]") + " " + this.task;
    }

}
