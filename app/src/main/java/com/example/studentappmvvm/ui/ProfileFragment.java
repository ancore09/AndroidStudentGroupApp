package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentProfileBinding mBinding;
    private AccountPrefsFragment accountPrefsFragment = new AccountPrefsFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.accountbtn.setOnClickListener(this);
        mBinding.appearencebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accountbtn:
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    //((MainActivity) requireActivity()).navView.setVisibility(View.GONE);
                    ((MainActivity) requireActivity()).showPrefs(accountPrefsFragment);
                }
                break;
            case R.id.appearencebtn:
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }
}
