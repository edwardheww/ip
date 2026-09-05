package walle;

import java.io.IOException;

import walle.exceptions.WALLEException;
import walle.parser.CommandType;
import walle.parser.Parser;
import walle.storage.Storage;
import walle.task.Task;
import walle.task.TaskList;
import walle.ui.Ui;

/**
 * Core of the WALLE chatbot. Wires together the {@link Ui}, {@link Storage},
 * {@link Parser}, and {@link TaskList} components, loads any previously saved
 * tasks, and turns one line of user input into a response at a time via
 * {@link #getResponse(String)} -- used by both the CLI loop in {@link #main}
 * and the JavaFX GUI.
 */
// CHECKSTYLE.OFF: AbbreviationAsWordInName - "WALLE" is the chatbot's actual name, not an abbreviation to shorten.
public class WALLE {
    // CHECKSTYLE.ON: AbbreviationAsWordInName

    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private final String greeting;

    /**
     * Creates a WALLE instance: sets up its components and loads any
     * previously saved tasks, folding any load error into the greeting so
     * both the CLI and the GUI surface it the same way.
     */
    public WALLE() {
        this.ui = new Ui();
        this.storage = new Storage("src/main/data/memory.txt");
        this.parser = new Parser();

        String greetingText = ui.formatGreeting();
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (WALLEException e) {
            loadedTasks = new TaskList();
            greetingText += ui.formatErrorMsg(e);
        } catch (Exception e) {
            loadedTasks = new TaskList();
            greetingText += ui.formatErrorMsg(e);
        }
        this.tasks = loadedTasks;
        this.greeting = greetingText;
    }

    /**
     * Starts WALLE's text-based command-line interface.
     *
     * @param args unused command-line arguments.
     */
    public static void main(String[] args) {
        new WALLE().runCli();
    }

    // Drive the CLI loop until the user says 'bye'
    private void runCli() {
        System.out.println(greeting);

        String input = ui.readCommand();
        while (!input.equals("bye")) {
            System.out.println(getResponse(input));
            input = ui.readCommand();
        }

        System.out.println(ui.formatFarewell());
        ui.closeScanner();
    }

    /**
     * Returns WALLE's greeting, e.g. to show as the first message in a GUI.
     *
     * @return the greeting message.
     */
    public String getGreeting() {
        return greeting;
    }

    /**
     * Processes one line of user input and returns WALLE's response,
     * persisting any change to the task list as a side effect.
     *
     * @param input the raw command string entered by the user.
     * @return the response to show the user.
     */
    public String getResponse(String input) {
        try {
            CommandType type = parser.parseCommandType(input);

            switch (type) {
                case LIST:
                    return ui.formatTaskList(tasks.getTasks());

                case MARK: {
                    String response = ui.formatTaskMarkedUpdate(tasks.mark(parser.parseIndex(input)));
                    updateMemoryFile();
                    return response;
                }

                case UNMARK: {
                    String response = ui.formatTaskUnmarkedUpdate(tasks.unmark(parser.parseIndex(input)));
                    updateMemoryFile();
                    return response;
                }

                case DELETE: {
                    int pos = parser.parseIndex(input);
                    Task tmp = tasks.delete(pos);
                    updateMemoryFile();
                    return ui.formatTaskDeletionUpdate(tmp, tasks.size());
                }

                case TODO:
                case DEADLINE:
                case EVENT: {
                    Task newTask = parser.parseTask(type, input);
                    tasks.add(newTask);
                    updateMemoryFile();
                    return ui.formatTaskAdditionUpdate(newTask, tasks.size());
                }

                case FIND: {
                    String keyword = parser.parseKeyword(input);
                    return ui.formatMatchingTasks(tasks.find(keyword));
                }

                default:
                    return "";
            }
        } catch (WALLEException e) {
            return ui.formatErrorMsg(e);
        } catch (RuntimeException e) {
            // Catches bugs such as an out-of-range task index, so a caller (CLI or GUI)
            // gets a visible message instead of the whole interface crashing/hanging.
            return ui.formatErrorMsg(e);
        }
    }

    // Persist any changes to the task list made during the session
    private void updateMemoryFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            System.out.println(ui.formatErrorMsg(e));
        }
    }

}
