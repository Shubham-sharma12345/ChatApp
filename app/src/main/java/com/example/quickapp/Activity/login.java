package com.example.quickapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    TextView Signup;
    TextView signin;
EditText loginemail,loginpassword;
String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Signup =findViewById(R.id.loginbutton2);
        auth=FirebaseAuth.getInstance();
        loginemail=findViewById(R.id.logimemail);
        loginpassword=findViewById(R.id.loginpassword);
        signin=findViewById((R.id.loginbutton));

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginemail.getText().toString();
                String password=loginpassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(login.this,"Enter valid data",Toast.LENGTH_SHORT).show();
                }else if (!email.matches(EMAIL_PATTERN)){
                    loginemail.setError("Invalid Email");
                    Toast.makeText(login.this,"Invalid email",Toast.LENGTH_SHORT ).show();
                }else if (password.length()<6){
                    loginpassword.setError("Invalid Password");
                    Toast.makeText(login.this,"Please Enter valid password",Toast.LENGTH_SHORT ).show();
                }else {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(login.this, MainActivity.class));
                            }else{
                                Toast.makeText(login.this,"Error is found",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, Register.class));
            }
        });

    }
}