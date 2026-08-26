public abstract class Task {

    private String task;
    private boolean checked;

    public Task(String task) {
        this.task = task;
        this.checked = false;
    }

    public Task(String task, boolean checked) {
        this.task = task;
        this.checked = checked;
    }

    public void check() {
        this.checked = true;
    }

    public void uncheck() {
        this.checked = false;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public String getTask() {
        return this.task;
    }

    public abstract String getMemoryFormat();

    private String getCheckbox() {
        return this.checked ? "[X]" : "[ ]";
    }

    @Override
    public String toString() {
        return this.getCheckbox() + " " + this.task;
    }

}
