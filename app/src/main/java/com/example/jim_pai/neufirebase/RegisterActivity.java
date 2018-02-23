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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextReenter;
    EditText editDisplayName;
    TextView textViewStatus;
    private FirebaseAuth mAuth;
    String[] slots;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    private enum SLOT {
        DISPLAY_NAME, EMAIL, PASSWORD, REENTER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        slots = new String[4];
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewStatus = findViewById(R.id.textViewStatus);
        editTextReenter = findViewById(R.id.reEnter);
        editDisplayName = findViewById(R.id.displayName);
        mAuth = FirebaseAuth.getInstance();

        editDisplayName.addTextChangedListener(new GenericTextWatcher(textViewStatus));
        editTextEmail.addTextChangedListener(new GenericTextWatcher(textViewStatus));
        editTextPassword.addTextChangedListener(new GenericTextWatcher(textViewStatus));
        editTextReenter.addTextChangedListener(new GenericTextWatcher(textViewStatus));
    }

    public void signUp(View view) {
        slots[SLOT.DISPLAY_NAME.ordinal()] = editDisplayName.getText().toString();
        slots[SLOT.PASSWORD.ordinal()] = editTextPassword.getText().toString();
        slots[SLOT.EMAIL.ordinal()] = editTextEmail.getText().toString();
        slots[SLOT.REENTER.ordinal()] = editTextReenter.getText().toString();
        if(hasEmptySlot(slots)) {
            showErrorMessage("Please fill all slots.");
            return;
        }

        if(!passwordMatched(slots[SLOT.PASSWORD.ordinal()], slots[SLOT.REENTER.ordinal()])) {
            showErrorMessage("Passwords are not matched");
            return;
        }

        mAuth.createUserWithEmailAndPassword(slots[SLOT.EMAIL.ordinal()], slots[SLOT.PASSWORD.ordinal()]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Create account successfully!", Toast.LENGTH_SHORT).show();
                    signIn();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
                else {
                    showErrorMessage("Fail to create account.");
                }
            }
        });
    }

    public void signIn() {
        mAuth.signInWithEmailAndPassword(slots[SLOT.EMAIL.ordinal()], slots[SLOT.PASSWORD.ordinal()]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    updateDisplayName();
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void updateDisplayName() {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(slots[SLOT.DISPLAY_NAME.ordinal()]).build();

        user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Name updates : " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    signOut();
                }
                else {
                    showErrorMessage("Unable to update name");
                }
            }
        });
    }

    public boolean hasEmptySlot(String[] slots) {
        for(int i = 0; i < slots.length; i++) {
            if(TextUtils.isEmpty(slots[i])) {
                Log.i("empty", SLOT.values()[i].toString());
                return true;
            }
        }
        return false;
    }

    public boolean passwordMatched(String pass1, String pass2) {
        if(pass1.length() != pass2.length())
            return false;

        for(int i = 0; i < pass1.length(); i++) {
            if(pass1.charAt(i) != pass2.charAt(i))
                return false;
        }

        return true;
    }

    public void showErrorMessage(String message) {
        textViewStatus.setText(message);
        textViewStatus.setTextColor(Color.RED);
    }
}
