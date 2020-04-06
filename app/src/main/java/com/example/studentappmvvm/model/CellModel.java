package com.example.studentappmvvm.model;

import com.evrencoskun.tableview.sort.ISortableModel;

public class CellModel implements ISortableModel {
    private String mId;
    private Object mData;

    public CellModel(String pId, Object mData) {
        this.mId = pId;
        this.mData = mData;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public Object getContent() {
        return mData;
    }

    public Object getData() {
        return mData;
    }
}
