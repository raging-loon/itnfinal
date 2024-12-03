package org.clevernameinc.itnfinal.forms;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.util.ArrayList;

///
/// @brief
///     Custom Table Cell that supports better style changes
///     This was mainly implemented to reduce code duplication
///
/// @note
///     Partial template specialization is nice...but where are muh type aliases?
///
public class RevisionCell<Product, T> extends TextFieldTableCell<Product, T> {
    /// Non-owning reference to a list of cells that have been changed
    private final ArrayList<RevisionCell<Product, ?>> cellChangeList;
    private boolean isMarked;
    public RevisionCell(ArrayList<RevisionCell<Product, ?>> list, StringConverter<T> converter) {
        super(converter);
        cellChangeList = list;
        isMarked = false;
    }

    @Override
    public void commitEdit(T t) {
        super.commitEdit(t);
        setChanged();
        if(!cellChangeList.contains(this))
            cellChangeList.add(this);
    }

    public void resetCell() {
        setStyle("");
        isMarked = false;
    }

    public void setChanged() {
        setStyle("-fx-background-color: lightblue;");
        isMarked = true;
    }
}
