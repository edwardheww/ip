public class ToDo extends Task {

    public ToDo(String task) {
        super(task);
    }

    public ToDo(String task, boolean checked) {
        super(task, checked);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
