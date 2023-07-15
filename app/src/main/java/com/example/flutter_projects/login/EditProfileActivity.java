package com.example.flutter_projects.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.flutter_projects.R;
import com.example.flutter_projects.firebase.FirebaseHelper;
import com.example.flutter_projects.firebase.User;
import com.google.firebase.firestore.DocumentSnapshot;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtName;
    EditText edtEmail;
    EditText edtMobile;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtPhone);
        btnSave = findViewById(R.id.btnSave);
        // Shared preference to get uid and pass it to getUserInfo
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "null");
        Log.e("document uid", "is : " + uid);
        if (uid == null) {
            Log.e("document", "is : " + null);
        } else {
            User user = FirebaseHelper.getUserInfo(EditProfileActivity.this, uid);
            Log.e("document user", "is : " + user.getMobile());
            edtMobile.setText(user.getMobile());
            edtEmail.setText(user.getEmail());
            edtName.setText(user.getName());
        }
    }
}