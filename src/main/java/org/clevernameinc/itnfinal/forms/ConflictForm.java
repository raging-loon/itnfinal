package org.clevernameinc.itnfinal.forms;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import org.clevernameinc.itnfinal.Staging.Change;
import org.clevernameinc.itnfinal.Staging.ChangeMap;

import java.util.ArrayList;

///
/// @brief
///     Merge conflict form
///
public class ConflictForm implements ConflictActionManager{

    private ArrayList<ChangeMap> mChangeList;
    private ArrayList<Change> mOutChangeList;
    @FXML
    private TableView<ChangeMap> mChangeView;

    @FXML
    private TableColumn<ChangeMap, Integer> mIDColumn;

    @FXML
    private TableColumn<ChangeMap, String> mServerValue;

    @FXML
    private TableColumn<ChangeMap, String> mEditValue;

    @FXML
    private TableColumn<ChangeMap, String> mOldValue;

    @FXML
    private TableColumn<ChangeMap, String> mTargetColumn;

    @FXML
    private TableColumn<ChangeMap, String> mOptionsColumn;

    public void initialize() {
        mIDColumn.setCellValueFactory(new PropertyValueFactory<>("PrimaryKey"));
        mServerValue.setCellValueFactory(new PropertyValueFactory<>("ServerValue"));
        mEditValue.setCellValueFactory(new PropertyValueFactory<>("NewValue"));
        mOldValue.setCellValueFactory(new PropertyValueFactory<>("OldValue"));
        mTargetColumn.setCellValueFactory(new PropertyValueFactory<>("FieldName"));
        /// Cell that contains buttons
        mOptionsColumn.setCellFactory(column -> new ConflictOptionCell<>(new DefaultStringConverter(), this));

        mOutChangeList = new ArrayList<>();
    }

    public void setChangeList(ArrayList<ChangeMap> changeMapList) {
        mChangeList = changeMapList;
        mChangeView.setItems(FXCollections.observableArrayList(mChangeList));
    }



    public ArrayList<Change> getChangeList() {
        return mOutChangeList;
    }

    @Override
    public void mergeSelected(ChangeMap map) {
        mOutChangeList.add(
                new Change(
                        map.getPrimaryKey(),
                        map.getFieldName(),
                        map.getNewValue(),
                        map.getServerValue(),
                        map.getColumnNumber()
                )
        );
        deleteSelected(map);
    }

    @Override
    public void deleteSelected(ChangeMap map) {
        mChangeList.remove(map);
        mChangeView.setItems(FXCollections.observableArrayList(mChangeList));
    }

    @FXML
    public void okButtonPressed() {
        closeWindow();
    }

    @FXML
    public void deleteAllButtonPressed() {
        mOutChangeList.clear();
        closeWindow();

    }

    @FXML
    public void mergeAllButtonPressed() {
        for(ChangeMap c : mChangeList) {
            mergeSelected(c);
        }
        closeWindow();

    }

    private void closeWindow() {
        Stage mStage = (Stage)(mChangeView.getScene().getWindow());
        mStage.close();
    }
}

