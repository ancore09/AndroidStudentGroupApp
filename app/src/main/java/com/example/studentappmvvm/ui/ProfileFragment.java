package com.example.studentappmvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentProfileBinding;
import com.example.studentappmvvm.viewmodel.ProfileViewModel;

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

        ProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        mBinding.setUser(viewModel.getUser());

        mBinding.accountbtn.setOnClickListener(this);
        mBinding.appearencebtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity() instanceof AppActivity) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((AppActivity) requireActivity()).navView.setVisibility(View.VISIBLE);
                ((AppActivity) requireActivity()).curr = this;
            }
        } else {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((TeacherAppActivity) requireActivity()).navView.setVisibility(View.VISIBLE);
                ((TeacherAppActivity) requireActivity()).curr = this;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accountbtn:
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {

                    ((AppActivity) requireActivity()).navView.setVisibility(View.GONE);

                    //((AppActivity) requireActivity()).showPrefs(accountPrefsFragment);
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
                break;
            case R.id.appearencebtn:
                ((AppActivity) requireActivity()).performTransition(new TableFragment(), this);
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
