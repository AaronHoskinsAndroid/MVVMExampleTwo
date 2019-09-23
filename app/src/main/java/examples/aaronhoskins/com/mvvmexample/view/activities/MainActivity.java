package examples.aaronhoskins.com.mvvmexample.view.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import examples.aaronhoskins.com.mvvmexample.R;
import examples.aaronhoskins.com.mvvmexample.databinding.ActivityMainBinding;
import examples.aaronhoskins.com.mvvmexample.viewmodel.UserLoginViewModel;

import static examples.aaronhoskins.com.mvvmexample.model.datasource.local.constants.AnalyticConstants.MIXPANEL_TOKEN;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    UserLoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //setContentView(R.layout.activity_main);
        Log.d("TAG", "onCreate: " + getSharedPreferences("pref", MODE_PRIVATE).getString("push_key", ""));
        viewModel = new UserLoginViewModel();
        activityMainBinding.setViewmodel(viewModel);
        viewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if(user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", user);
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initPushNotificationChannel();
        }
    }

    @BindingAdapter("android:text")
    public static void setLoginText(TextView view, String message) {
        view.setText(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initPushNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel
                = new NotificationChannel("channel",
                "messages", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }
}
