package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.evrencoskun.tableview.TableView;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.model.GroupEntity;
import com.example.studentappmvvm.viewmodel.TableFragmentViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TableFragment extends Fragment {

    private static final String LOG_TAG = TableFragment.class.getSimpleName();

    private TableView mTableView;
    private TableAdapter mTableAdapter;
    private ProgressBar mProgressBar;
    private MaterialSpinner mSpinner;
    private TableFragmentViewModel mainViewModel;
    private AtomicInteger currGroupId = new AtomicInteger();

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        mSpinner = view.findViewById(R.id.spinner);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTableView = view.findViewById(R.id.my_TableView);

        // initialize ViewModel
        mainViewModel = new ViewModelProvider(requireActivity()).get(TableFragmentViewModel.class);
        currGroupId.set(mainViewModel.getGroups().getValue().get(0).getID());

        ArrayList<String> groupNames = mainViewModel.getGroups().getValue().stream().map(GroupEntity::getName).collect(Collectors.toCollection(ArrayList::new));
        mSpinner.setItems(groupNames);
        mSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            mainViewModel.getGroups().getValue().forEach(groupEntity -> {
                if (groupEntity.getName().equals(item)) {
                    currGroupId.set(groupEntity.getID());
                }
            });
            mainViewModel.updateTable(currGroupId.get());
        });

        initializeTableView(mTableView);
        showProgressBar();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeUI();
    }

    private void subscribeUI() {
        mainViewModel.getUsers().observe(getViewLifecycleOwner(), userEntities -> {
            if(userEntities != null && userEntities.size()>0){
                // set the list on TableFragmentViewModel
                mTableAdapter.setUserList(userEntities, mainViewModel.getLessons().getValue().stream().filter(lessonEntity -> {
                    if (lessonEntity.getGroupID() == currGroupId.get()) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList()));
                mTableView.sortColumn(mainViewModel.column, mainViewModel.type);
                hideProgressBar();
            }

            /*new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if(userEntities != null && userEntities.size()>0 && userEntities.get(userEntities.size()-1).getMarks() != null){
                        // set the list on TableFragmentViewModel
                        mTableAdapter.setUserList(userEntities, vMainViewModel.getLessons().getValue());
                        hideProgressBar();
                    }
                }
            }.start();*/
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAppActivity) requireActivity()).curr = this;
    }

    private void initializeTableView(TableView tableView){

        // Create TableView Adapter
        mTableAdapter = new TableAdapter(getContext());
        tableView.setAdapter(mTableAdapter);

        // Create listener
        tableView.setTableViewListener(new TableViewListener(tableView, mainViewModel, getActivity(), getChildFragmentManager()));
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.INVISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTableView.setVisibility(View.VISIBLE);
    }
}
