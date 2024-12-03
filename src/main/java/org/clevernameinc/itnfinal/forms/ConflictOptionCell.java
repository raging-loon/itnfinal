package org.clevernameinc.itnfinal.forms;

import javafx.scene.control.Button;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

///
/// @brief
///     Custom Table Cell for holding merge options in the ConflictForm
///
public class ConflictOptionCell<ChangeMap, T> extends TextFieldTableCell<ChangeMap, T> {

    private final ConflictActionManager cam;

    public ConflictOptionCell(StringConverter<T> converter, ConflictActionManager cam) {
        super(converter);
        this.cam = cam;

    }

    @Override
    public void updateItem(T t, boolean b) {
        super.updateItem(t, b);
        if(!b) {

            Button merge = new Button("Merge");
            merge.setOnAction(event -> {
                cam.mergeSelected((org.clevernameinc.itnfinal.Staging.ChangeMap) this.getTableRow().getItem());
            });


            Button delete = new Button("Delete");
            delete.setOnAction(event -> {
                cam.deleteSelected((org.clevernameinc.itnfinal.Staging.ChangeMap) this.getTableRow().getItem());
            });


            HBox box = new HBox(merge, delete);

            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }
}
