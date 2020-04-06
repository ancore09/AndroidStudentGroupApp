package com.example.studentappmvvm.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.ITableViewListener;

public class TableViewListener implements ITableViewListener {
    private static final String LOG_TAG = TableViewListener.class.getSimpleName();

    private ITableView mTableView;

    public TableViewListener(ITableView pTableView) {
        this.mTableView = pTableView;
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        Log.d(LOG_TAG, "onCellClicked has been clicked for x= " + column + " y= " + row);
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        Log.d(LOG_TAG, "onCellLongPressed has been clicked for " + row);
    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {
        Log.d(LOG_TAG, "onColumnHeaderClicked has been clicked for " + column);
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {
        if (columnHeaderView != null && columnHeaderView instanceof ColumnHeaderViewHolder) {

            // Create Long Press Popup
            ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup((ColumnHeaderViewHolder) columnHeaderView, mTableView);
            // Show
            popup.show();
        }
    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        Log.d(LOG_TAG, "onRowHeaderClicked has been clicked for " + row);
    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder owHeaderView, int row) {
        Log.d(LOG_TAG, "onRowHeaderLongPressed has been clicked for " + row);
    }
}
