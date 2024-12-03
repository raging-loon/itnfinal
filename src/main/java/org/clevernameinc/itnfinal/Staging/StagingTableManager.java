package org.clevernameinc.itnfinal.Staging;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class StagingTableManager {

    private final TableView<Change> mChangeTableView;

    private final TableColumn<Change, Integer> mIDColumn;

    private final TableColumn<Change, String> mNewValueColumn;

    private final TableColumn<Change, String> mOldValueColumn;

    private final TableColumn<Change, String> mTargetColumn;

    /// IntelliJ is a lifesaver
    public StagingTableManager(TableView<Change> mChangeTableView, TableColumn<Change, Integer> mIDColumn, TableColumn<Change, String> mNewValueColumn, TableColumn<Change, String> mOldValueColumn, TableColumn<Change, String> mTargetColumn) {
        this.mChangeTableView = mChangeTableView;
        this.mIDColumn = mIDColumn;
        this.mNewValueColumn = mNewValueColumn;
        this.mOldValueColumn = mOldValueColumn;
        this.mTargetColumn = mTargetColumn;

        // id
        mIDColumn.setCellValueFactory(new PropertyValueFactory<>("PrimaryKey"));
        mNewValueColumn.setCellValueFactory(new PropertyValueFactory<>("NewValue"));
        mOldValueColumn.setCellValueFactory(new PropertyValueFactory<>("OldValue"));
        mTargetColumn.setCellValueFactory(new PropertyValueFactory<>("FieldName"));
    }

    public void updateChangeTable(ArrayList<Change> changes) {
        mChangeTableView.setItems(
                FXCollections.observableArrayList(changes)
        );
    }


}
