package com.example.studentappmvvm.ui;

import com.example.studentappmvvm.model.CellModel;
import com.example.studentappmvvm.model.ColumnHeaderModel;
import com.example.studentappmvvm.model.Lesson;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class TableViewModel {

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    private List<ColumnHeaderModel> createColumnHeaderModelList(List<LessonEntity> lessonList) {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
//        list.add(new ColumnHeaderModel("Id"));
//        list.add(new ColumnHeaderModel("Group"));
//        list.add(new ColumnHeaderModel("Date"));
//        list.add(new ColumnHeaderModel("Time"));
//        list.add(new ColumnHeaderModel("Theme"));

        lessonList.forEach(lessonEntity -> {
            list.add(new ColumnHeaderModel(lessonEntity.getDate()));
        });

        return list;
    }

    private List<List<CellModel>> createCellModelList(List<Mark> markList, int lesAmount) {
        List<List<CellModel>> lists = new ArrayList<>();

        Mark m =new Mark();
        m.setId(34);
        m.setMark("23");
        markList.add(m);

        // Creating cell model list from User list for Cell Items
        // In this example, Lesson list is populated from web service

        for (int i = 0; i < markList.size(); i += lesAmount) {

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;

            for (int j  = 0; j < lesAmount; j++) {
                try {
                    list.add(new CellModel("1-" + i+j, markList.get(i+j).getMark()));
                } catch (Exception e) {}
            }

//            list.add(new CellModel("2-" + i, lessonEntity.getGroupName()));
//            list.add(new CellModel("3-" + i, lessonEntity.getDate()));
//            list.add(new CellModel("4-" + i, lessonEntity.getTime()));
//            list.add(new CellModel("5-" + i, lessonEntity.getTheme()));

            // Add
            lists.add(list);
        }

        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }

    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }

    public void generateListForTableView(List<Mark> markList, List<LessonEntity> lessonList) {
        mColumnHeaderModelList = createColumnHeaderModelList(lessonList);
        mCellModelList = createCellModelList(markList, lessonList.size());
        mRowHeaderModelList = createRowHeaderList(markList.size());
    }
}
