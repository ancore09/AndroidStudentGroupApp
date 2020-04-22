package com.example.studentappmvvm.ui;

import com.example.studentappmvvm.model.CellModel;
import com.example.studentappmvvm.model.ColumnHeaderModel;
import com.example.studentappmvvm.model.Lesson;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.RowHeaderModel;
import com.example.studentappmvvm.model.UserEntity;

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
            if (lessonEntity.getGroupID() == 1) {
                list.add(new ColumnHeaderModel(lessonEntity.getDate()));
            }
        });

        return list;
    }

    private List<List<CellModel>> createCellModelList(List<UserEntity> userEntities, int lesAmount) {
        List<List<CellModel>> lists = new ArrayList<>();


        // Creating cell model list from User list for Cell Items
        // In this example, Lesson list is populated from web service

        for (int i = 0; i < userEntities.size(); i++) {

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;



//            list.add(new CellModel("2-" + i, lessonEntity.getGroupName()));
//            list.add(new CellModel("3-" + i, lessonEntity.getDate()));
//            list.add(new CellModel("4-" + i, lessonEntity.getTime()));
//            list.add(new CellModel("5-" + i, lessonEntity.getTheme()));

            // Add
            for (int j = 0; j < lesAmount-2; j++) {
                try {
                    list.add(new CellModel(String.valueOf(i), userEntities.get(i).getMarks().get(j).getMark()));
                } catch (Exception e) {
                    list.add(new CellModel(String.valueOf(i), "N"));
                }
            }
            lists.add(list);
        }

        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(List<UserEntity> userEntities) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < userEntities.size(); i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(userEntities.get(i).getFirstName() + userEntities.get(i).getFirstName()));
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

    public void generateListForTableView(List<UserEntity> userEntities, List<LessonEntity> lessonList) {
        mColumnHeaderModelList = createColumnHeaderModelList(lessonList);
        mCellModelList = createCellModelList(userEntities, lessonList.size());
        mRowHeaderModelList = createRowHeaderList(userEntities);
    }
}
