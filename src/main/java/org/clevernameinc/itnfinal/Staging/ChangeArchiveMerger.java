package org.clevernameinc.itnfinal.Staging;

import javafx.scene.control.TableView;
import org.clevernameinc.itnfinal.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

///
/// @brief
///     Attempts to merge a change list with the current table
///     This will store any conflicts for the client to resolve
///
public class ChangeArchiveMerger {
    /// non-owning
    private final ArrayList<Change> mChangeList;

    /// non-owning
    private final TableView<Product> mProductTableView;

    /// non-owning
    private final TableView<Change> mChangeTableView;

    ///
    /// @brief
    ///     If any change in the change archive has an old value
    ///     that does not match the current tables value,
    ///     it will be present here
    ///
    private final ArrayList<ChangeMap> mConflictList;

    public ChangeArchiveMerger(ArrayList<Change> changeList, TableView<Product> mProductTableView, TableView<Change> mChangeTableView) {
        this.mChangeList = changeList;
        this.mProductTableView = mProductTableView;
        this.mChangeTableView = mChangeTableView;

        mConflictList = new ArrayList<>();
    }

    public ArrayList<ChangeMap> getConflictList() {
        return mConflictList;
    }

    ///
    /// @brief
    ///     A merge conflict will occur if the "old value" from the archive is not
    ///     equal to the current value of the table UNLESS the "new value" in the archive
    ///     is equal to the current value.
    ///
    ///     e.g. If my change looks like "old archive val = 1", "new archive val = 2", "server val = 3"
    ///     Then there is a merge conflict.
    ///
    ///     If my change looks like "old archive val = 1", "new archive val = 3", "server val = 3"
    ///     Then there is no conflict
    ///
    ///     If my change looks like "old archive val = 3", "new archive val = 2", "server val = 3"
    ///     Then there is no conflict
    ///
    public void mergeChanges() {

        var changeIdmap = createIDMapFromChangeList(mChangeList);

        for(Product row : mProductTableView.getItems()) {

            if(changeIdmap.containsKey(row.getId())) {
                Logger.getGlobal().info("Found changes for row " + row.getId());
                for(Change c : changeIdmap.get(row.getId())) {

                    int col = c.getColumnNumber();

                    Object cellValue = mProductTableView.getColumns().get(col).getCellObservableValue(row).getValue();

                    // if the archived change's new value is equal to the cell's current value, disregard
                    if(cellValue.equals(c.getNewValue()))
                        continue;

                    if(!cellValue.toString().equals(c.getOldValue()))
                        mConflictList.add(new ChangeMap(cellValue.toString(), c));
                    else {
                        mChangeTableView.getItems().add(c);
                    }

                }

            }
        }

    }

    ///
    /// @brief
    ///     Create a mapping of Change.mPrimaryKey => ArrayList<Change>
    ///     This will be iterated over in mergeChanges
    private HashMap<Integer, ArrayList<Change>> createIDMapFromChangeList(ArrayList<Change> list) {
        HashMap<Integer, ArrayList<Change>> changeMap = new HashMap<>();

        for(Change c : list) {

            if(!changeMap.containsKey(c.getPrimaryKey())) {
                changeMap.put(c.getPrimaryKey(), new ArrayList<>());
            }

            changeMap.get(c.getPrimaryKey()).add(c);
        }
        return changeMap;
    }
}
