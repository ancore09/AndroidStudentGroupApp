package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentChatBinding;
import com.example.studentappmvvm.model.MemberDataEntity;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.viewmodel.ChatViewModel;

import java.util.List;
import java.util.Random;

public class ChatFragment extends Fragment {
    public static final String TAG = "ChatFragment";
    int k =0;

    MessageAdapter mMessageAdapter;
    FragmentChatBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        mMessageAdapter = new MessageAdapter();
        mBinding.messagesList.setAdapter(mMessageAdapter);
        mBinding.setIsLoading(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChatViewModel viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        mBinding.sendbtn.setOnClickListener(v -> {
            sendMessage(mBinding.editText.getText().toString(), viewModel);
        });
        subscribeUI(viewModel.getMessages());
    }

    private void subscribeUI(MediatorLiveData<List<MessageEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), mMessages -> {
            if (mMessages != null) {
                mBinding.setIsLoading(false);
                mMessageAdapter.setMessagesList(mMessages);
                Log.d("VIEW", "OBSERVED MODEL IN VIEW");
                mMessageAdapter.notifyDataSetChanged();
            } else {
                mBinding.setIsLoading(true);
            }
            mBinding.executePendingBindings();
            mBinding.messagesList.scrollToPosition(mMessageAdapter.getItemCount()-1);
        });
    }

    void sendMessage(String text, ChatViewModel viewModel) {
        if (text.length() > 0) {
            mBinding.editText.getText().clear();
            boolean btu = false;
            if (text.charAt(0) == '#') {
                btu = true;
                text = text.replace("#", "");
            }
            viewModel.sendMessage(new MessageEntity(k, text, new MemberDataEntity(getRandomName(), getRandomColor()), btu));
            k++;
            mBinding.messagesList.scrollToPosition(mMessageAdapter.getItemCount()-1);
        }
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mMessageAdapter = null;
        super.onDestroyView();
    }

    private String getRandomName() {
        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};
        return (adjs[(int) Math.floor(Math.random() * adjs.length)] + "_" + nouns[(int) Math.floor(Math.random() * nouns.length)]);
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
