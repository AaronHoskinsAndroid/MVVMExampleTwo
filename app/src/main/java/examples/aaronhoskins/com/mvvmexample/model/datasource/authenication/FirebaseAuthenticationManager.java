package examples.aaronhoskins.com.mvvmexample.model.datasource.authenication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import examples.aaronhoskins.com.mvvmexample.model.user.UserLogin;

public class FirebaseAuthenticationManager {
    public static final String TAG = "FB_AUTH_MNGR";
    FirebaseAuth mAuth;
    IFirebaseAuthManager callback;

    public FirebaseAuthenticationManager(IFirebaseAuthManager callback) {
        mAuth = FirebaseAuth.getInstance();
        this.callback = callback;
    }

    public void signInToFirebaseAccount(UserLogin userLogin) {
        final String email = userLogin.getEmail();
        final String password = userLogin.getPassword();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSignInUser(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            callback.onSignInUser(null);
                        }
                    }
                });
    }

    public void createFirebaseAccount(final UserLogin userLogin) {
        final String email = userLogin.getEmail();
        final String password = userLogin.getPassword();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSignInUser(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            callback.onSignInUser(null);
                        }
                    }
                });
    }

    public void signOutOfFirebaseAccount() {
        mAuth.signOut();
    }

    public interface IFirebaseAuthManager{
        void onSignInUser(FirebaseUser user);
    }
}
