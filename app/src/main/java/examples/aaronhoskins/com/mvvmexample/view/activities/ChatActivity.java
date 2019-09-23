package examples.aaronhoskins.com.mvvmexample.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import examples.aaronhoskins.com.mvvmexample.R;
import examples.aaronhoskins.com.mvvmexample.databinding.ActivityChatBinding;
import examples.aaronhoskins.com.mvvmexample.model.chatmessage.ChatMessage;
import examples.aaronhoskins.com.mvvmexample.view.adapters.ChatMessageAdapter;
import examples.aaronhoskins.com.mvvmexample.viewmodel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    ChatViewModel viewModel;
    ActivityChatBinding binding;
    ChatMessageAdapter chatMessageAdapter;
    RecyclerView rvMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        binding.setLifecycleOwner(this);
        viewModel = new ChatViewModel();
        binding.setChatViewModel(viewModel);
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getExtras();
            FirebaseUser user = bundle.getParcelable("user");
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
            viewModel.setUser(user);
        }
        viewModel.messages.observe(this, new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> messageArrayList) {
                if(chatMessageAdapter == null) {
                    chatMessageAdapter = new ChatMessageAdapter(messageArrayList);
                    rvMessageList = findViewById(R.id.rvChatMessagesList);
                    rvMessageList.setLayoutManager(new LinearLayoutManager(rvMessageList.getContext()));
                    rvMessageList.setAdapter(chatMessageAdapter);

                } else {
                    chatMessageAdapter.onMessageListUpdate(messageArrayList);
                }
            }
        });
    }
}
