public abstract class Task {

    private String task;
    private boolean isDone;

    public Task(String task) {
        this.task = task;
        this.isDone = false;
    }

    public Task(String task, boolean isDone) {
        this.task = task;
        this.isDone = isDone;
    }

    public void check() {
        this.isDone = true;
    }

    public void uncheck() {
        this.isDone = false;
    }

    public boolean isChecked() {
        return this.isDone;
    }

    public String getTask() {
        return this.task;
    }

    public abstract String getMemoryFormat();

    private String getCheckbox() {
        return this.isDone ? "[X]" : "[ ]";
    }

    @Override
    public String toString() {
        return this.getCheckbox() + " " + this.task;
    }

}
