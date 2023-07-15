package com.example.flutter_projects.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flutter_projects.MainActivity;
import com.example.flutter_projects.R;
import com.example.flutter_projects.core.ProgressHelper;
import com.example.flutter_projects.core.ProgressiveIndicator;
import com.example.flutter_projects.firebase.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    // variable for FirebaseAuth class
    private FirebaseAuth firebaseAuth;
    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;
    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;
    // string for storing our verification ID
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize firebase
        FirebaseHelper.init();
        // initializing variables for button and Edittext.
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        edtOTP.setVisibility(View.INVISIBLE);
        verifyOTPBtn.setVisibility(View.INVISIBLE);

        // setting onclick listener for generate OTP button.
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking whether the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(LoginActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    String phone = "+91" + edtPhone.getText().toString();
                    ProgressHelper.showDialog(LoginActivity.this, "Otp sending...");
                    sendVerificationCode(phone);
                }
            }
        });
        // initializing on click listener
        // for verify otp button
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(LoginActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    ProgressHelper.showDialog(LoginActivity.this, "Verifying otp..");
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        FirebaseHelper.firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    ProgressHelper.dismissDialog();
                    Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_LONG).show();
                    FirebaseHelper.updateUserInfo(task.getResult().getUser().getUid(), task.getResult().getUser().getPhoneNumber());

                    SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("uid",task.getResult().getUser().getUid());
                    editor.apply();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    ProgressHelper.dismissDialog();
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        try {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseHelper.firebaseAuth).setPhoneNumber(number)            // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            Log.e("sendVerificationCode", e.toString());
        }


    }


    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            generateOTPBtn.setVisibility(View.GONE);
            verifyOTPBtn.setVisibility(View.VISIBLE);
            edtPhone.setVisibility(View.GONE);
            edtOTP.setVisibility(View.VISIBLE);
            ProgressHelper.dismissDialog();
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //in on start method checking if the user is already sign in.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //if the user is not null then we are opening a main activity on below line.
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }

    }
}