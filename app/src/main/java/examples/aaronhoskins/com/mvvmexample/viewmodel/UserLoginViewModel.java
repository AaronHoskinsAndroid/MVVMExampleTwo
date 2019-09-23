package examples.aaronhoskins.com.mvvmexample.viewmodel;

import android.provider.BaseColumns;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import examples.aaronhoskins.com.mvvmexample.BR;
import examples.aaronhoskins.com.mvvmexample.R;
import examples.aaronhoskins.com.mvvmexample.model.datasource.authenication.FirebaseAuthenticationManager;
import examples.aaronhoskins.com.mvvmexample.model.user.UserLogin;

import static examples.aaronhoskins.com.mvvmexample.model.datasource.local.constants.AnalyticConstants.MIXPANEL_TOKEN;

public class UserLoginViewModel extends ViewModel
        implements Observable, FirebaseAuthenticationManager.IFirebaseAuthManager {
    PropertyChangeRegistry propertyChangeRegistry;
    private static final String TAG = "UserLoginViewModel";
    @Bindable
    public String userEmail;
    @Bindable
    public String userPassword;
    @Bindable
    public String loginMessage = "Please Enter Login Information Below";

    MixpanelAPI mixpanelAPI;
    MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();

    private UserLogin userLogin;
    private FirebaseAuthenticationManager authManager;

    public UserLoginViewModel() {
        propertyChangeRegistry = new PropertyChangeRegistry();
        authManager = new FirebaseAuthenticationManager(this);
    }

    public void onLogInClicked(View view) {
        if(mixpanelAPI == null) {
            mixpanelAPI = MixpanelAPI.getInstance(view.getContext(), MIXPANEL_TOKEN);
        }
        if(!userEmail.isEmpty() && !userPassword.isEmpty()) {
            mixpanelAPI.timeEvent("LOGIN_DURATION");
            userLogin = new UserLogin(userEmail, userPassword);
            switch (view.getId()) {
                case R.id.btnLogIntoAccount:
                    //mixpanelAPI.track("LOGIN_ACCOUNT");
                    JSONObject props = new JSONObject();
                    try {
                        props.put("user_name", userEmail);
                        mixpanelAPI.track("USER_LOGIN", props);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    authManager.signInToFirebaseAccount(userLogin);
                    break;
                case R.id.btnCreateAccount:
                    mixpanelAPI.track("CREATE_ACCOUNT");
                    authManager.createFirebaseAccount(userLogin);
                    break;
            }
            mixpanelAPI.track("LOGIN_DURATION");
//            Toast.makeText(view.getContext(), userLogin.getEmail(), Toast.LENGTH_LONG).show();
//            loginMessage = userLogin.getEmail();
//            notifySingleFieldChange(examples.aaronhoskins.com.mvvmexample.BR.loginMessage);

        }
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(Editable userEmail) {
        this.userEmail = userEmail.toString();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(Editable userPassword) {
        this.userPassword = userPassword.toString();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.remove(callback);
    }

    public void notifySingleFieldChange(final int fieldId) {
        propertyChangeRegistry.notifyChange(this, fieldId);
    }

    public void notifyAllFieldsChanged() {
        propertyChangeRegistry.notifyChange(this, 0);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    @Override
    public void onSignInUser(FirebaseUser user) {
        final String message = user != null ? user.getEmail() : "NOT SIGNED IN";
        Log.d(TAG, "onSignInUser: " + message);
        userLiveData.postValue(user);
    }
}
