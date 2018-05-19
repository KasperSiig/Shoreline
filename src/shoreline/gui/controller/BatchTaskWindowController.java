/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author madst
 */
public class BatchTaskWindowController implements Initializable, IController {

    @FXML
    private ScrollPane scrlPanePen;
    @FXML
    private VBox vBoxPen;

    /* Java Variables */
    private List<BatchView> selectedBatches;

    private ContextMenu cMenu;

    private ModelManager model;

    public BatchTaskWindowController() {
        this.cMenu = new ContextMenu();
        this.selectedBatches = new ArrayList();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setBatches(selectedBatches, model.getBatchModel().getBatches(), vBoxPen);

        genRightClickDel();
    }

    private void setBatches(List<BatchView> selected, ObservableList<Batch> batches, VBox vBox) {
        setBatchView(selected, batches, vBox);
        batches.addListener((ListChangeListener.Change<? extends Batch> c) -> {
            while (c.next()) {
                if (c.wasAdded() || c.wasPermutated() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                    setBatchView(selected, batches, vBox);
                }
            }
        });
    }

    /**
     * Generates BatchViews for List
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

    private BatchView makeBatchView(List<BatchView> selectedb, VBox vBox, Batch batch) {
        BatchView batchView = new BatchView(batch);
        batchView.postInit(model);
        Tooltip tt = new Tooltip(batch.getTargetDir().toString());
        Tooltip.install(batchView, tt);
        batchView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    cMenu.hide();
                    if (event.isControlDown()) {
                        if (selectedb.contains(batchView)) {
                            toggleSelected(batchView, selectedb, false);
                        } else {
                            toggleSelected(batchView, selectedb, true);
                        }
                    } else if (event.isShiftDown()) {
                        toggleSelected(batchView, selectedb, true);
                        int id1 = Integer.valueOf(selectedb.get(0).getId());

                        if (selectedb.size() > 1) {
                            int id2 = Integer.valueOf(selectedb.get(selectedb.size() - 1).getId());

                            List<BatchView> allBatches = new ArrayList();

                            vBox.getChildren().forEach((node) -> {
                                allBatches.add((BatchView) node);
                            });

                            for (BatchView batch : allBatches) {
                                if (Integer.valueOf(batch.getId()) > id1 && Integer.valueOf(batch.getId()) < id2) {
                                    toggleSelected(batch, selectedb, true);
                                } else if (Integer.valueOf(batch.getId()) < id1 && Integer.valueOf(batch.getId()) > id2) {
                                    toggleSelected(batch, selectedb, true);
                                }
                            }
                        }

                    } else {
                        if (selectedb.contains(batchView)) {
                            clearSelected(selectedb);

                        } else {
                            clearSelected(selectedb);
                            toggleSelected(batchView, selectedb, true);
                        }
                    }

                    if (event.getClickCount() % 2 == 0) {
                        try {
                            String temp = batch.getTargetDir().getParentFile().getPath();
                            temp = temp.replaceAll("\\\\", "/");
                            Desktop.getDesktop().browse(new URI(temp));
                        } catch (URISyntaxException | IOException ex) {
                            Logger.getLogger(TaskWindowController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    cMenu.show(vBox, event.getScreenX(), event.getScreenY());
                    if (!selectedb.contains(batchView)) {
                        toggleSelected(batchView, selectedb, true);
                    }
                }
            }
        }
        );
        return batchView;
    }

    private void toggleSelected(BatchView batchView, List<BatchView> selectedBatches, boolean selected) {
        if (selected) {
            selectedBatches.add(batchView);
            batchView.getStyleClass().add("selectedBorder");
            System.out.println("selected");
        } else {
            selectedBatches.remove(batchView);
            batchView.getStyleClass().remove(0);
        }
    }

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
                if (openConfirmWindow("Are you sure you want to delete " + selectedBatches.size() + " batches?", false)) {
                    selectedBatches.forEach((batch) -> {
                        try {
                            model.getBatchModel().getBatches().remove(batch.getBatch());
                            model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has deleted task " + batch.getBatch().getName());
                        } catch (GUIException ex) {
                            Logger.getLogger(BatchTaskWindowController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    });
                    selectedBatches.clear();
                } else {
                    return;
                }
            } else {
                selectedBatches.forEach((batch) -> {
                    try {
                        model.getBatchModel().getBatches().remove(batch.getBatch());
                        model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has deleted task " + batch.getBatch().getName());
                    } catch (GUIException ex) {
                        Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                    }
                });
                selectedBatches.clear();
            }
        });
        cMenu.getItems().add(delItem);
    }

    /**
     * Open confirm window
     *
     * @param msg
     * @param txtField
     * @return
     */
    private boolean openConfirmWindow(String msg, boolean txtField) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(msg, txtField);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Confirmation");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            return cwc.getConfirmation();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
        return false;
    }

}
