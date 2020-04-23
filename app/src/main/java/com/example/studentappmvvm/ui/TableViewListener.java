package com.example.studentappmvvm.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.model.CellModel;
import com.example.studentappmvvm.viewmodel.TableFragmentViewModel;

import java.util.function.Consumer;

public class TableViewListener implements ITableViewListener {
    private static final String LOG_TAG = TableViewListener.class.getSimpleName();

    private ITableView mTableView;
    private TableFragmentViewModel viewModel;
    private Context mContext;
    private FragmentManager transitionManager;

    public TableViewListener(ITableView pTableView, TableFragmentViewModel viewModel, Context context, FragmentManager transitionManager) {
        this.mTableView = pTableView;
        this.viewModel = viewModel;
        this.mContext = context;
        this.transitionManager = transitionManager;
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        Log.d(LOG_TAG, "onCellClicked has been clicked for x= " + column + " y= " + row);

        //mTableView.getAdapter().changeCellItem(column, row, new CellModel("q", "5"));
        viewModel.column = column;
        viewModel.row = row;
        viewModel.type = mTableView.getSortingStatus(column);
        new MarkEditDialogFragment(mark -> {
            viewModel.setMark(column, row, mark);
            Toast.makeText(mContext, mark, Toast.LENGTH_LONG).show();
        }).show(transitionManager, "editMark");
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
            ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup((ColumnHeaderViewHolder) columnHeaderView, mTableView, viewModel);
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
