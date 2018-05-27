package shoreline.gui.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.be.Batch;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class BatchTaskWindowController implements Initializable, IController {

    @FXML
    private ScrollPane scrlPanePen;
    @FXML
    private VBox vBoxPen;

    private List<BatchView> selectedBatches;
    private ContextMenu contextMenu;
    private ModelManager model;

    public BatchTaskWindowController() {
        this.contextMenu = new ContextMenu();
        this.selectedBatches = new ArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setBatches(selectedBatches, model.getBatchModel().getBatches(), vBoxPen);

        genRightClickDel();
    }

    /**
     * Generates all batches, and adds a listener to them
     *
     * @param selectedBatches
     * @param batches
     * @param vBox
     */
    private void setBatches(List<BatchView> selectedBatches, ObservableList<Batch> batches, VBox vBox) {
        setBatchView(selectedBatches, batches, vBox);
        batches.addListener((ListChangeListener.Change<? extends Batch> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    setBatchView(selectedBatches, batches, vBox);
                }
            }
        });
    }

    /**
     * Generates BatchViews for List of batches
     *
     * @param model
     */
    private void setBatchView(List<BatchView> selectedBatches, ObservableList<Batch> batch, VBox vBox) {
        int i = 0;
        vBox.getChildren().clear();
        for (Batch batchItems : batch) {
            BatchView batchView = makeBatchView(selectedBatches, vBox, batchItems);
            batchView.setId(String.valueOf(i));
            vBox.getChildren().add(batchView);
            i++;
        }
    }

    /**
     * Sets up visual information, and sets OnClick action
     *
     * @param selectedBatches
     * @param vBox
     * @param batch
     * @return
     */
    private BatchView makeBatchView(List<BatchView> selectedBatches, VBox vBox, Batch batch) {
        BatchView batchView = new BatchView(batch);
        batchView.postInit(model);
        Tooltip toolTip = new Tooltip(batch.getTargetDir().toString());
        Tooltip.install(batchView, toolTip);
        batchView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleRightClick(event);
                handleDoubleClick(event);
                handleSelectionMethods(event);
            }

            /**
             * Handles opening contextMenu when right clicking
             *
             * @param event
             */
            private void handleRightClick(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    contextMenu.show(vBox, event.getScreenX(), event.getScreenY());
                    if (!selectedBatches.contains(batchView)) {
                        toggleSelected(batchView, selectedBatches, true);
                    }
                }
            }

            /**
             * Handles double clicking
             *
             * @param event
             */
            private void handleDoubleClick(MouseEvent event) {
                if (event.getClickCount() % 2 == 0) {
                    try {
                        String temp = batch.getTargetDir().getParentFile().getPath();
                        temp = temp.replaceAll("\\\\", "/");
                        Desktop.getDesktop().browse(new URI(temp));
                    } catch (URISyntaxException | IOException ex) {
                        Window.openExceptionWindow("Something went wrong opening folder");
                    }
                }
            }

            /**
             * Handles everything related to selection model
             *
             * @param event
             */
            private void handleSelectionMethods(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    contextMenu.hide();
                    if (event.isControlDown()) {
                        handleCtrlDown();
                    } else if (event.isShiftDown()) {
                        handleShiftDown();
                    } else {
                        if (selectedBatches.contains(batchView)) {
                            clearSelected(selectedBatches);
                        } else {
                            clearSelected(selectedBatches);
                            toggleSelected(batchView, selectedBatches, true);
                        }
                    }
                }
            }

            /**
             * Handles what happens when shift is down
             */
            private void handleShiftDown() {
                toggleSelected(batchView, selectedBatches, true);
                int id1 = Integer.valueOf(selectedBatches.get(0).getId());

                if (selectedBatches.size() > 1) {
                    int id2 = Integer.valueOf(selectedBatches.get(selectedBatches.size() - 1).getId());

                    List<BatchView> allBatches = new ArrayList();

                    vBox.getChildren().forEach((node) -> {
                        allBatches.add((BatchView) node);
                    });

                    allBatches.forEach((batch) -> {
                        if (Integer.valueOf(batch.getId()) > id1 && Integer.valueOf(batch.getId()) < id2) {
                            toggleSelected(batch, selectedBatches, true);
                        } else if (Integer.valueOf(batch.getId()) < id1 && Integer.valueOf(batch.getId()) > id2) {
                            toggleSelected(batch, selectedBatches, true);
                        }
                    });
                }
            }

            /**
             * Handles what happens when Ctrl is down
             */
            private void handleCtrlDown() {
                if (selectedBatches.contains(batchView)) {
                    toggleSelected(batchView, selectedBatches, false);
                } else {
                    toggleSelected(batchView, selectedBatches, true);
                }
            }
        }
        );
        return batchView;
    }

    /**
     * Toggle the BatchView selection on or off
     *
     * @param batchView BatchView to toggle selection on
     * @param selectedBatches List of selected tasks
     * @param selected Whether to toggle it selected or not
     */
    private void toggleSelected(BatchView batchView, List<BatchView> selectedBatches, boolean selected) {
        if (selected) {
            selectedBatches.add(batchView);
            batchView.getStyleClass().add("selectedBorder");
        } else {
            selectedBatches.remove(batchView);
            batchView.getStyleClass().remove(0);
        }
    }

    /**
     * Toggles selection off for all selected views
     *
     * @param batchView
     */
    private void clearSelected(List<BatchView> batchView) {
        List<BatchView> temp = new ArrayList(batchView);
        temp.forEach((batchViewTemp) -> {
            toggleSelected(batchViewTemp, batchView, false);
        });
    }

    /**
     * Generates MenuItem for delete batch
     */
    private void genRightClickDel() {
        MenuItem delItem = new MenuItem("Delete selected batch");
        delItem.setOnAction((event) -> {
            if (selectedBatches.size() > 1) {
                if (openConfirmWindow("Are you sure you want to delete " + selectedBatches.size() + " batches?")) {
                    delSelectedBatches();
                }
            } else {
                delSelectedBatches();
            }
        });
        contextMenu.getItems().add(delItem);
    }

    private void delSelectedBatches() {
        selectedBatches.forEach((batch) -> {
            try {
                model.getBatchModel().getBatches().remove(batch.getBatch());
                model.getLogModel().add(model.getUserModel().getUser(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getFirstName() + " has deleted task " + batch.getBatch().getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        });
        selectedBatches.clear();
    }

    /**
     * Open confirm window
     *
     * @param message 
     * @return
     */
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
