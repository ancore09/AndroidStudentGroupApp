package com.example.studentappmvvm.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.model.CellModel;
import com.example.studentappmvvm.model.ColumnHeaderModel;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.RowHeaderModel;
import com.example.studentappmvvm.model.UserEntity;

import java.util.List;

public class TableAdapter extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel, CellModel> {
    private TableViewModel myTableViewModel;

    public TableAdapter(Context context) {
        super(context);

        this.myTableViewModel = new TableViewModel();
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        CellModel cell = (CellModel) cellItemModel;

        if (holder instanceof CellViewHolder) {
            // Get the holder to update cell item text
            ((CellViewHolder) holder).setCellModel(cell, columnPosition);
        }
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_column_header_layout, parent, false);

        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        ColumnHeaderModel columnHeader = (ColumnHeaderModel) columnHeaderItemModel;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader, columnPosition);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout, parent, false);
        setRowHeaderWidth(100);
        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        RowHeaderModel rowHeaderModel = (RowHeaderModel) rowHeaderItemModel;

        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderModel.getData());
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false);
    }

    public void setUserList(List<UserEntity> userEntities, List<LessonEntity> lessonEntities) {
        // Generate the lists that are used to TableViewAdapter
        myTableViewModel.generateListForTableView(userEntities, lessonEntities);

        // Now we got what we need to show on TableView.
        setAllItems(myTableViewModel.getColumHeaderModeList(), myTableViewModel.getRowHeaderModelList(), myTableViewModel.getCellModelList());
    }
}
