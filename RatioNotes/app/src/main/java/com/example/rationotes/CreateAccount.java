package com.example.rationotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {
    EditText email,passET,confpassET;
    Button CreateAcc;
    TextView LoginBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email=findViewById(R.id.emailId);
        passET=findViewById(R.id.passwordET);
        confpassET=findViewById(R.id.conpasswordET);
        CreateAcc=findViewById(R.id.creatacc);
        LoginBTN=findViewById(R.id.login_btn);

        CreateAcc.setOnClickListener(v -> createAccount());
        LoginBTN.setOnClickListener(v -> finish());


    }
    void createAccount(){
        String username= email.getText().toString();
        String pass= passET.getText().toString();
        String confpass = confpassET.getText().toString();

        //validation method call
        boolean isvalidated=validateData(username,pass,confpass);
        if(!isvalidated){
            return;
        }
        CreateAccountInFirebase(username,pass);


    }
    void CreateAccountInFirebase(String username,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Account created
                            utility.showToast(CreateAccount.this, "Account is created succesfully");

                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();


                        }else{
                            //Failure
                            utility.showToast(CreateAccount.this,task.getException().getLocalizedMessage());


                        }
                    }
                });

    }

    boolean validateData(String username,String pass,String confpass){
        //Validate data that Entered by user
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            email.setError("Username lenght is invalid");
            return false;

        }
        if(pass.length()<6){
            passET.setError("Password length is invalid ");
            return false;
        }
        if(!pass.equals(confpass)){
            confpassET.setError("Password didn't matched ");
            return false;
        }
        return true;
    }
}