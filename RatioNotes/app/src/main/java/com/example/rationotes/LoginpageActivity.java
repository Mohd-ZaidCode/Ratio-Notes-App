package com.example.rationotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginpageActivity extends AppCompatActivity {
    EditText email,passET;
    Button loginbt;
    TextView create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        email=findViewById(R.id.emailId);
        passET=findViewById(R.id.passwordET);
        loginbt=findViewById(R.id.loginBT);
        create=findViewById(R.id.createacc);




        loginbt.setOnClickListener((v)-> loginuser());

        create.setOnClickListener(v->startActivity(new Intent(LoginpageActivity.this,CreateAccount.class)));
    }

    void loginuser(){
        String username= email.getText().toString();
        String pass= passET.getText().toString();
        boolean isvalidated=validateData(username,pass);
        if(!isvalidated){
            return;
        }
        loginAccountInFirebase(username,pass);

    }
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                //Login succesfully
                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(LoginpageActivity.this,MainActivity.class));

                }else{
                    utility.showToast(LoginpageActivity.this,"Email is not verified! Please verify your email.");

                }
            }else{
                //Login fails
                utility.showToast(LoginpageActivity.this,task.getException().getLocalizedMessage());
            }
            }
        });
    }
    boolean validateData(String username,String pass){
        //Validate data that Entered by user
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            email.setError("Email is not valid");
            return false;

        }
        if(pass.length()<6){
            passET.setError("Password length is invalid ");
            return false;
        }

        return true;}
}