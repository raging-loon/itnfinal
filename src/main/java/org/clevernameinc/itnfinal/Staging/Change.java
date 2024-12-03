package org.clevernameinc.itnfinal.Staging;

import java.io.Serializable;

///
/// @brief
///     Represent some change to the table
///
public class Change implements Serializable {


    private final String mNewValue;
    private final String mOldValue;
    private final int mPrimaryKey;

    private final int mColNum;
    private final String mFieldName;

    public Change(int mPrimaryKey,String mFieldName, String mNewValue, String mOldValue, int mColNum) {
        this.mNewValue = mNewValue;
        this.mOldValue = mOldValue;
        this.mPrimaryKey = mPrimaryKey;
        this.mColNum = mColNum;
        this.mFieldName = mFieldName;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public int getPrimaryKey() {
        return mPrimaryKey;
    }

    public String getOldValue() {
        return mOldValue;
    }

    public String getNewValue() {
        return mNewValue;
    }

    public int getColumnNumber() {
        return mColNum;
    }

}
