package org.clevernameinc.itnfinal.Staging;

///
/// @brief
///     A change and the updated server value
///     WAS going to be a hash map but those are much
///     harder to integrate with TableViews
///
public class ChangeMap {
    private final String mServerValue;

    private final Change mChange;

    public ChangeMap(String serverValue, Change c) {
        mChange = c;
        mServerValue = serverValue;
    }

    public final String getServerValue() {
        return mServerValue;
    }

    public final String getFieldName() {
        return mChange.getFieldName();
    }

    public final int getPrimaryKey() {
        return mChange.getPrimaryKey();
    }

    public final String getOldValue() {
        return mChange.getOldValue();
    }

    public final String getNewValue() {
        return mChange.getNewValue();
    }

    public final int getColumnNumber() {
        return mChange.getColumnNumber();
    }

}
