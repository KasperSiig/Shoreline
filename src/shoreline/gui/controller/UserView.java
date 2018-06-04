package shoreline.gui.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.be.User;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class UserView extends BorderPane {

    private ModelManager model;
    private User user;
    private VBox curVBox;
    private Label curLabel;

    @FXML
    private Label lblFirstName;
    @FXML
    private Label lblLastName;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblUserLevel;

    public UserView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/UserView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.user = user;
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void postInit(ModelManager model) {
        this.model = model;

    }

    /**
     * Sets the visual info in the BatchView
     *
     * @param user
     * @param vbox
     */
    public void setInfo(User user, VBox vbox) {
        lblFirstName.setText("First Name: " + user.getFirstName());
        lblLastName.setText("Last Name: " + user.getLastName());
        lblUsername.setText("Username: " + user.getUserName());
        lblPassword.setText("Password: •••••••••••");
        lblUserLevel.setText("User Level: " + user.getUserLevel());
        this.curVBox = vbox;
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (openConfirmWindow("Are you sure you want to delete user: " + user.getUserName())) {
            try {
                model.getUserModel().deleteUser(user);
                curVBox.getChildren().remove(this);
            } catch (GUIException ex) {
                Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Node node = event.getPickResult().getIntersectedNode();
            toggleSelectedOn(node);
        }
    }

    private void toggleSelectedOn(Node node) {
        Label label = (Label) node.getParent();
        VBox parent = (VBox) label.getParent();
        int index = parent.getChildren().indexOf(label);
        String title = label.getText().split(":")[0] + ": ";
        String input = label.getText().split(":")[1].trim();

        if (title.equals("Password: ")) {
            JFXPasswordField txtField = new JFXPasswordField();
            txtField.setText(input);
            txtField.setMaxWidth(label.getWidth());
            txtField.setMaxHeight(label.getHeight());
            setTxtFieldListener(txtField, label, title, parent, index);
        } else {
            JFXTextField txtField = new JFXTextField(input);
            txtField.setMaxWidth(label.getWidth());
            txtField.setMaxHeight(label.getHeight());
            setTxtFieldListener(txtField, label, title, parent, index);
        }

    }

    private void setTxtFieldListener(JFXTextField txtField, Label label, String title, VBox parent, int index) {
        txtField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    label.setText(title + txtField.getText());
                    String newInput = label.getText().split(":")[1].trim();
                    switch (title) {
                        case "First Name: ":
                            user.setFirstName(newInput);
                            break;
                        case "Last Name: ":
                            user.setLastName(newInput);
                            break;
                        case "Username: ":
                            user.setUserName(newInput);
                            break;
                        case "User Level: ":
                            user.setUserLevel(Integer.parseInt(newInput));
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    model.getUserModel().updateUser(user);
                    parent.getChildren().remove(txtField);
                    parent.getChildren().add(index, label);
                } catch (GUIException ex) {
                    Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                parent.getChildren().remove(txtField);
                parent.getChildren().add(index, label);
            }
            if (title.equals("User Level: ")) {
                txtField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.isEmpty()) {
                        return;
                    }
                    if (!newValue.matches("[0-9]+")) {
                        txtField.setText(oldValue);
                    }
                });
            }
        });
        txtField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && parent.getChildren().contains(txtField)) {
                parent.getChildren().remove(txtField);
                parent.getChildren().add(index, label);
            }
        });
        parent.getChildren().remove(label);
        parent.getChildren().add(index, txtField);
        txtField.requestFocus();
        txtField.end();
    }

    private void setTxtFieldListener(JFXPasswordField txtField, Label label, String title, VBox parent, int index) {
        txtField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    model.getUserModel().updateUser(user, txtField.getText());

                    parent.getChildren().remove(txtField);
                    parent.getChildren().add(index, label);
                } catch (GUIException ex) {
                    Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                parent.getChildren().remove(txtField);
                parent.getChildren().add(index, label);
            }
        });
        txtField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && parent.getChildren().contains(txtField)) {
                parent.getChildren().remove(txtField);
                parent.getChildren().add(index, label);
            }
        });
        parent.getChildren().remove(label);
        parent.getChildren().add(index, txtField);
        txtField.requestFocus();
        txtField.end();
    }

    private boolean openConfirmWindow(String message) {
        boolean yes = false;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(message);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Confirmation");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            yes = cwc.getConfirmation();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
        return yes;
    }
}
