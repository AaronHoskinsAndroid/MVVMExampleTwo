package examples.aaronhoskins.com.mvvmexample.viewmodel;

import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import examples.aaronhoskins.com.mvvmexample.model.chatmessage.ChatMessage;

public class ChatViewModel extends ViewModel {
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;

    public MutableLiveData<String> currentMessageEntered = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();
    public ChatViewModel() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("chat_log");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ChatMessage> currentListOfValues = messages.getValue();
                if(currentListOfValues == null) {
                    currentListOfValues = new ArrayList<>();
                }
                Iterator<DataSnapshot> messageIterator = dataSnapshot.getChildren().iterator();
                while (messageIterator.hasNext()) {
                    DataSnapshot shot = messageIterator.next();
                    ChatMessage message = shot.getValue(ChatMessage.class);
                    currentListOfValues.add(message);
                }
                messages.postValue(currentListOfValues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled: ", databaseError.toException());
            }
        });
    }

    public void afterMessageEntered(Editable message) {
        currentMessageEntered.postValue(message.toString());
    }

    public void onSendMessageClicked(View view) {
        ChatMessage message;
        final String currentTime = Calendar.getInstance().getTime().toString();
        final String currentUserId = user.getEmail();

        if(currentMessageEntered != null) {
            message = new ChatMessage(currentUserId, currentTime, currentMessageEntered.getValue());
            reference.child(currentTime).setValue(message);
        }
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
