package au.com.mysites.camps.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import au.com.mysites.camps.R;
import au.com.mysites.camps.util.Constants;
import au.com.mysites.camps.util.Debug;
import au.com.mysites.camps.util.UtilDialog;
import au.com.mysites.camps.util.UtilGeneral;
import au.com.mysites.camps.util.UtilImage;

/**
 * Firebase Authentication using a Google ID Token.
 */

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.main_status);
        mDetailTextView = findViewById(R.id.main_detail);

        // Progress Dialog
        mProgressDialog = new ProgressDialog(this);

        // Button listeners
        findViewById(R.id.main_sign_in_button).setOnClickListener(this);
        findViewById(R.id.main_sign_out_button).setOnClickListener(this);
        findViewById(R.id.main_disconnect_button).setOnClickListener(this);
        findViewById(R.id.main_start_app).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            String path = UtilImage.getRealPathFromUri(this, personPhoto);
            if (!UtilGeneral.stringEmpty(path)) {
                Log.d(TAG,"User picture filename: " + path);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ImageView imageView = findViewById(R.id.main_background_imageView);
                if (imageView != null)
                    Log.d (TAG, "bitmap not empty");
                   // imageView.setImageBitmap(bitmap);
                Glide.with(MainActivity.this).load(personPhoto).into(imageView);
             /*   // Create local file with directory and name of the file
                final File localFile = new File(storageDir, path);

                if (localFile.exists()) {
                    // File exists on local device
                    if (Debug.DEBUG_UTIL) Log.d(TAG, "file exists: " + localFile.toString());
                    // Display the file
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                } */


            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        /* Check if called by SummarySitesActivity and if there was a request to sign out,
         * if there was, stay in this activity, otherwise go to SummarySitesActivity  */
        Intent intent = getIntent();
        boolean signOutRequest;
        if (intent != null) {
            signOutRequest = intent.getBooleanExtra(getString(R.string.intent_sign_out), false);

            if (!signOutRequest) { //if no request go to summary site activity
                Intent SummarySite = new Intent(MainActivity.this, SummarySitesActivity.class);
    //kk            startActivity(SummarySite);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        UtilDialog.hideProgressDialog(mProgressDialog);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        UtilDialog.showProgressDialog(mProgressDialog);
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        UtilDialog.hideProgressDialog(mProgressDialog);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (Debug.DEBUG_METHOD_ENTRY) Log.d(TAG, "updateUI()");

        UtilDialog.hideProgressDialog(mProgressDialog);
        if (user != null) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.main_sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.main_sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.main_sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.main_sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    /*
     * Dialog to prompt user for yes/no if they want to exit the activity.
     */
    @Override
    public void onBackPressed() {
        if (Debug.DEBUG_METHOD_ENTRY_SITE) Log.d(TAG, "onBackPressed()");

        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.warning)
                .setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.sure))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    @Override
    public void onClick(View v) {
        if (Debug.DEBUG_METHOD_ENTRY) Log.d(TAG, "onClick()");

        int i = v.getId();
        if (i == R.id.main_sign_in_button) {
            signIn();
        } else if (i == R.id.main_sign_out_button) {
            signOut();
        } else if (i == R.id.main_start_app) {
            Intent startApp = new Intent(MainActivity.this, SummarySitesActivity.class);
            startActivity(startApp);
        } else if (i == R.id.main_disconnect_button) {
            revokeAccess();
        }
    }
}
