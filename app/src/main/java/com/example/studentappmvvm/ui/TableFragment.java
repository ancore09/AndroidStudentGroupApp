package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.evrencoskun.tableview.TableView;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.viewmodel.TableFragmentViewModel;

public class TableFragment extends Fragment {

    private static final String LOG_TAG = TableFragment.class.getSimpleName();

    private TableView mTableView;
    private TableAdapter mTableAdapter;
    private ProgressBar mProgressBar;
    private TableFragmentViewModel vMainViewModel;

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

        mProgressBar = view.findViewById(R.id.progressBar);

        mTableView = view.findViewById(R.id.my_TableView);

        initializeTableView(mTableView);


        // initialize ViewModel
        vMainViewModel = new ViewModelProvider(requireActivity()).get(TableFragmentViewModel.class);

        vMainViewModel.getLessons().observe(getViewLifecycleOwner(), lessonEntities -> {

            if(lessonEntities != null && lessonEntities.size()>0){
                // set the list on TableFragmentViewModel
                mTableAdapter.setUserList(lessonEntities);
                hideProgressBar();
            }
        });

        // Let's post a request to get the User data from a web server.
        postRequest();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppActivity) requireActivity()).curr = this;
    }

    private void initializeTableView(TableView tableView){

        // Create TableView Adapter
        mTableAdapter = new TableAdapter(getContext());
        tableView.setAdapter(mTableAdapter);

        // Create listener
        tableView.setTableViewListener(new TableViewListener(tableView));
    }


    private void postRequest(){
        showProgressBar();
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
