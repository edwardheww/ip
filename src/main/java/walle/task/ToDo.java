package walle.task;

public class ToDo extends Task {

    public ToDo(String task) {
        super(task);
    }

    public ToDo(String task, boolean checked) {
        super(task, checked);
    }

    public String getMemoryFormat() {
        return "T;"
                + (super.isChecked() ? "X" : " ") + ";"
                + super.getTask();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
