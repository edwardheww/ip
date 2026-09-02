package walle.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import walle.exception.CorruptMemoryException;
import walle.exception.WALLEException;
import walle.task.Deadline;
import walle.task.Event;
import walle.task.Task;
import walle.task.ToDo;

/**
 * Handles reading tasks from, and writing tasks to, the memory file on disk.
 */
public class Storage {

    private final String filePath;

    /**
     * Creates a Storage backed by the memory file at the given path.
     *
     * @param filePath path to the memory file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks previously saved to the memory file, creating the file
     * (and any missing parent directories) if it does not yet exist.
     *
     * <p>
     * Expected line formats in the memory file:
     * <ul>
     * <li>ToDo: {@code <type>;<checkmark>;<task>}</li>
     * <li>Deadline: {@code <type>;<checkmark>;<task>;<endDT>}</li>
     * <li>Event: {@code <type>;<checkmark>;<task>;<startDT>;<endDT>}</li>
     * </ul>
     *
     * @return the list of tasks read from the memory file.
     * @throws FileNotFoundException if the memory file cannot be found.
     * @throws IOException           if the memory file cannot be created.
     * @throws WALLEException        if a line in the memory file is corrupted.
     */
    public ArrayList<Task> load() throws FileNotFoundException, IOException, WALLEException {
        ArrayList<Task> memory = new ArrayList<>();
        File memFile = new File(filePath);

        // Ensuring file exists by creating file if nonexistent
        if (!memFile.exists()) {
            File parent = memFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            memFile.createNewFile();
        }

        Scanner memScanner = new Scanner(memFile);

        while (memScanner.hasNext()) { // Handles saved tasks one by one
            try {
                String[] taskData = memScanner.nextLine().split(";");
                // Handling if saved task is a ToDo
                if (taskData[0].equals("T")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    memory.add(new ToDo(task, checked));
                }

                // Handling if saved task is a Deadline
                else if (taskData[0].equals("D")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    String endDT = taskData[3];
                    memory.add(new Deadline(task, LocalDateTime.parse(endDT), checked));
                }

                else if (taskData[0].equals("E")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    String startDT = taskData[3];
                    String endDT = taskData[4];
                    memory.add(new Event(task, LocalDateTime.parse(startDT), LocalDateTime.parse(endDT), checked));
                }
            } catch (Exception e) {
                memScanner.close();
                throw new CorruptMemoryException();
            }
        }

        memScanner.close();
        return memory;
    }

    /**
     * Overwrites the memory file with the current contents of the given task list.
     *
     * @param tasks the tasks to persist.
     * @throws IOException if the memory file cannot be written to.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        FileWriter memFw = new FileWriter(filePath);
        for (Task task : tasks) {
            String memInput = task.getMemoryFormat();
            memFw.write(memInput + System.lineSeparator());
        }
        memFw.close();
    }

}