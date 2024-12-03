package org.clevernameinc.itnfinal;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.clevernameinc.itnfinal.Staging.*;
import org.clevernameinc.itnfinal.db.DatabaseManager;
import org.clevernameinc.itnfinal.db.ProductLoader;
import org.clevernameinc.itnfinal.forms.ConflictForm;
import org.clevernameinc.itnfinal.forms.MessageBox;
import org.clevernameinc.itnfinal.forms.RevisionCell;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

///
/// @brief
///     Main Controller
///
/// @note
///     As it turns out, TableView handles sorting for you...
///
public class Controller {

    private Staging mStaging;

    /// Sub-controller for the staging table
    private StagingTableManager mSTM;

    /// Column Position -> Remote server column name
    private ArrayList<String> mServerNameMap;

    private ArrayList<RevisionCell<Product, ?>> mChangedCellList;

    @FXML
    private TableView<Product> mMainTableView;

    @FXML
    private TableColumn<Product, Integer> mIDColumn;

    @FXML
    private TableColumn<Product, String> mBrandColumn;

    @FXML
    private TableColumn<Product, String> mDescColumn;

    @FXML
    private TableColumn<Product, Double> mRatingColumn;

    @FXML
    private TableColumn<Product, Double> mPriceColumn;

    @FXML
    private TableColumn<Product, Double> mWeightColumn;

    @FXML
    private TableView<Change> subChangeTableView;

    @FXML
    private TableColumn<Change, Integer> subIDColumn;

    @FXML
    private TableColumn<Change, String> subNewValueColumn;

    @FXML
    private TableColumn<Change, String> subOldValueColumn;

    @FXML
    private TableColumn<Change, String> subTargetColumn;


    public void initialize() {

        mStaging = new Staging();

        mChangedCellList = new ArrayList<>();

        mServerNameMap = new ArrayList<>() {{
            add("p_id");
            add("p_brand");
            add("p_desc");
            add("p_price");
            add("p_weight");
            add("p_rating");
        }};
        /// yeah its a bit of repetition, but anything else would overcomplicate it

        mIDColumn.    setCellValueFactory(new PropertyValueFactory<>("Id"));
        mBrandColumn. setCellValueFactory(new PropertyValueFactory<>("Brand"));
        mPriceColumn. setCellValueFactory(new PropertyValueFactory<>("Price"));
        mDescColumn.  setCellValueFactory(new PropertyValueFactory<>("Desc"));
        mWeightColumn.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        mRatingColumn.setCellValueFactory(new PropertyValueFactory<>("Rating"));

        mBrandColumn. setCellFactory(column -> new RevisionCell<>(mChangedCellList, new DefaultStringConverter()));
        mDescColumn.  setCellFactory(column -> new RevisionCell<>(mChangedCellList, new DefaultStringConverter()));
        mRatingColumn.setCellFactory(column -> new RevisionCell<>(mChangedCellList, new DoubleStringConverter()));
        mPriceColumn. setCellFactory(column -> new RevisionCell<>(mChangedCellList, new DoubleStringConverter()));
        mWeightColumn.setCellFactory(column -> new RevisionCell<>(mChangedCellList, new DoubleStringConverter()));


        mSTM = new StagingTableManager(
                subChangeTableView,
                subIDColumn,
                subNewValueColumn,
                subOldValueColumn,
                subTargetColumn
        );

    }

    public void loadProductList(ArrayList<Product> plist) {

        assert plist != null;

        mMainTableView.setItems(
            FXCollections.observableArrayList(plist)
        );

    }

    ///
    /// @brief
    ///     Finalize this controller
    ///     That consists of
    ///         1. Checking for change archives
    ///
    public void finalizeController() {
        if(isChangeArchivePresent()) {
            MessageBox.Info("You have archived changes. These will be inserted into the staging table.\nAny conflicts will be presented for you to resolve");
            manageArchivedChanges();
        }
    }

    @FXML
    public void onEditCommit(TableColumn.CellEditEvent<Product, Object> event) {
        int col = event.getTablePosition().getColumn();
        assert col > 0 && col < mServerNameMap.size();

        String newValue = event.getNewValue().toString();

        // if this is 'brand' or 'description', wrap it in quotes or MySQL will have a stroke.
        if(col == 1 || col == 2)
            newValue = "\"" + newValue + "\"";

        Logger.getGlobal().info(
                String.format(
                        "Adding new change. Row ID=%d, Column Name=%s, oldValue=%s, newValue=%s",
                            event.getRowValue().getId(), mServerNameMap.get(col),event.getOldValue(), event.getNewValue()
                )
        );

        mStaging.addChange(
                new Change(
                        event.getRowValue().getId(),    // primary key
                        mServerNameMap.get(col),        // field name
                        newValue,                       // new value
                        event.getOldValue().toString(), // old value
                        col                             // column number
                )
        );

        mSTM.updateChangeTable(mStaging.getChangeList());
    }



    @FXML
    public void onUpdateButtonPress() {
        if(mStaging.getChangeList().isEmpty())
            return;

        try {
            mStaging.pushChanges();
            clearAllChanges();
            updateLocalTable();
        } catch(SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Update failed: " + e.getMessage()).showAndWait();
        }

    }


    ///
    /// @brief
    ///     Sync our table with the remote table
    /// @todo: Is this messy?
    ///
    private void updateLocalTable() {
        try {
            mMainTableView.setItems(
                    FXCollections.observableArrayList(
                            ProductLoader.loadProducts(
                                    DatabaseManager.getInstance().createStatement()
                            )
                    )
            );
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Could not sync with database: " + e.getMessage()).showAndWait();
        }
    }


    @FXML
    public void clearAllChanges() {

        for(var i : mChangedCellList)
            i.resetCell();

        mChangedCellList.clear();
        mStaging.clearChangeTable();
        mSTM.updateChangeTable(mStaging.getChangeList());

    }

    @FXML
    public void onSubTableUndoChangesClicked() {
        Change toUndo = subChangeTableView.getSelectionModel().getSelectedItem();

        for(var i : mChangedCellList) {
            if(i.getTableRow().getItem().getId() == toUndo.getPrimaryKey()){
                i.resetCell();
                mChangedCellList.remove(i);
                break;
            }
        }

        mStaging.removeChange(toUndo);
        mSTM.updateChangeTable(mStaging.getChangeList());
    }


    private boolean isChangeArchivePresent() {
        try {
            File f = new File("change.db");
            return f.exists() && !f.isDirectory();
        } catch(Exception e) {
            return false;
        }
    }

    private void manageArchivedChanges() {
        try {
             ChangeArchive ca = new ChangeArchive();

             ArrayList<Change> changes = ca.read();

             ChangeArchiveMerger cam = new ChangeArchiveMerger(
                     changes,
                     mMainTableView,
                     subChangeTableView
             );
             cam.mergeChanges();

             if(!cam.getConflictList().isEmpty())
                showConflictManagementForm(cam.getConflictList());


            File change = new File("change.db");

            change.delete();

        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, "Could not resolve archived changes: " + e.getMessage()).showAndWait();
        }

    }

    private void showConflictManagementForm(ArrayList<ChangeMap> list) throws IOException {
        // @todo: is this bad practice
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("forms/conflict-form.fxml"));
        Stage s = new Stage();

        Scene conflictScene = new Scene(loader.load());
        ConflictForm controller = loader.getController();
        controller.setChangeList(list);

        s.setScene(conflictScene);
        s.showAndWait();
        controller.getChangeList().forEach(e -> mStaging.addChange(e));
        mSTM.updateChangeTable(mStaging.getChangeList());
    }

}