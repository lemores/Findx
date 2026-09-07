package com.example.findx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
   SignInButton signinbutton;
    ViewFlipper flipper;
    GoogleApiClient mGoogleApiClient;
    public EditText email;
    public EditText senha;
    public Button conectarl;
    public FirebaseAuth firebaseauth;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.login);

        flipper = findViewById(R.id.flipper);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        conectarl = findViewById(R.id.conectarl);
        firebaseauth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        email.addTextChangedListener(loginTextWatcher);
        senha.addTextChangedListener(loginTextWatcher);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signinbutton = findViewById(R.id.loginbutton);
        signinbutton.setOnClickListener(this);

    }
    TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            conectarl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firebaseauth.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Seja Bem Vindo!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Login.this, MapsActivity.class));
                            } else {
                                Toast.makeText(Login.this, "Senha ou E-mail incorretos", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }
            });
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameinput = email.getText().toString().trim();
            String passwordinput = senha.getText().toString().trim();

            conectarl.setEnabled(!usernameinput.isEmpty() && !passwordinput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseauth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
    }

    public void voltar(View view){
        Intent Login = new Intent(getApplication(), Introduction.class);
        startActivity(Login);
    }
    private void signIn(){
        Intent SignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(SignInIntent, RC_SIGN_IN);
    }
    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(Login.this, "Você saiu da sua conta!",     Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginbutton:
                signIn();
                break;
            /*case R.id.signoutbutton:
                signOut();
                break;*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Intent intent = new Intent(Login.this, MapsActivity.class);
                startActivity(intent);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                //Google Sign In failed, update UI appropriately
                Toast.makeText(Login.this, "Seja Bem Vindo!",     Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseauth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(Login.this, "Seja Bem Vindo!",     Toast.LENGTH_LONG).show();
                            Intent Login = new Intent(getApplication(), MapsActivity.class);
                            startActivity(Login);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.loginbutton), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}

