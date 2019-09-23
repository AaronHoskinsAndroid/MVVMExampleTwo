package examples.aaronhoskins.com.mvvmexample.model.chatmessage;

import android.text.Editable;
import android.view.View;

import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {

    @SerializedName("emailFromID")
    private String emailFromID;
    @SerializedName("timeOfMessage")
    private String timeOfMessage;
    @SerializedName("message")
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String emailFromID, String timeOfMessage, String message) {
        this.emailFromID = emailFromID;
        this.timeOfMessage = timeOfMessage;
        this.message = message;
    }

    public String getEmailFromID() {
        return emailFromID;
    }

    public void setEmailFromID(String emailFromID) {
        this.emailFromID = emailFromID;
    }

    public String getTimeOfMessage() {
        return timeOfMessage;
    }

    public void setTimeOfMessage(String timeOfMessage) {
        this.timeOfMessage = timeOfMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
