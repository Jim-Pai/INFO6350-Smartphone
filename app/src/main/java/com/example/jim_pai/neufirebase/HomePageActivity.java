package com.example.jim_pai.neufirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePageActivity extends AppCompatActivity {
    private static final String TAG = "HomePageActivity";
    private static final String DB_REFERENCE = "SampleNode";
    TextView welcomeTextView;
    DatabaseReference dbRef;
    FirebaseDatabase database;
    EditText inputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        String welcome = getResources().getString(R.string.welcome);
        welcomeTextView.setText(welcome + " " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference(DB_REFERENCE);
        inputData = findViewById(R.id.dataInput);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                Toast.makeText(HomePageActivity.this, "Data updated : " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Fail to read value.", databaseError.toException());
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomePageActivity.this, MainActivity.class));
    }

    public void addData(View view) {
        dbRef.setValue(inputData.getText().toString());
    }

    public void goToProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String welcome = getResources().getString(R.string.welcome);
        welcomeTextView.setText(welcome + " " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }
}
