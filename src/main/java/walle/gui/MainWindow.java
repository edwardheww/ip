package walle.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import walle.WALLE;

/**
 * Controller for the main GUI window.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private WALLE walle;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private final Image walleImage = new Image(this.getClass().getResourceAsStream("/images/walle.png"));

    /**
     * Binds the scroll pane to auto-scroll to the bottom whenever new dialog is added.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the WALLE instance this window talks to, and shows its greeting.
     *
     * @param walle the WALLE instance to use.
     */
    public void setWalle(WALLE walle) {
        this.walle = walle;
        dialogContainer.getChildren().add(DialogBox.getWalleDialog(walle.getGreeting(), walleImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing WALLE's
     * reply, then appends them to the dialog container. Clears user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = walle.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getWalleDialog(response, walleImage));
        userInput.clear();
    }

}
