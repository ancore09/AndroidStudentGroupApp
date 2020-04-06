package com.example.studentappmvvm.ui;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.model.CellModel;

public class CellViewHolder extends AbstractViewHolder {
    public final TextView cell_textview;
    public final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = itemView.findViewById(R.id.cell_data);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCellModel(CellModel cellModel, int columnPosition) {
        cell_textview.setGravity(Gravity.CENTER_VERTICAL);

        cell_textview.setText(String.valueOf(cellModel.getData()));

        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();
    }

    @Override
    public void setSelected(SelectionState selectionState) {
        super.setSelected(selectionState);

        if (selectionState == SelectionState.SELECTED) {
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color.selected_text_color));
        } else {
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color.unselected_text_color));
        }
    }
}
