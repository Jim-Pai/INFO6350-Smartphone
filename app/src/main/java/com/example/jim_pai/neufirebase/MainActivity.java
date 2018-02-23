package com.example.jim_pai.neufirebase;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewStatus;
    Button btnRegister;
    Button btnLogin;
    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextEmail = findViewById(R.id.emailText);
        editTextPassword = findViewById(R.id.passwordText);
        textViewStatus = findViewById(R.id.textViewStatus);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail.addTextChangedListener(new GenericTextWatcher(textViewStatus));
        editTextPassword.addTextChangedListener(new GenericTextWatcher(textViewStatus));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    public void signIn(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        Log.i("email", email);
        Log.i("password", password);

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            String emptyMessage = getResources().getString(R.string.empty_message);
            showErrorMessage(emptyMessage);
//            Toast.makeText(this, emptyMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(mAuth.getCurrentUser() != null){
                    Log.i("login", "Successfully log in.");
                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                }
                else {
                    showErrorMessage("E-mail or Password is invalid.");
                    Log.e("login", "Fail log in.");
                }
            }
        });
    }

    public void goToSignUpPage(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void showErrorMessage(String message) {
        textViewStatus.setText(message);
        textViewStatus.setTextColor(Color.RED);
    }
}
