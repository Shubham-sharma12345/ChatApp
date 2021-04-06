package com.example.quickapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickapp.R;
import com.example.quickapp.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    EditText registername, registeremail, registerpassword, registerconfirmpassword;
    TextView registerUserBtn;
    TextView createAccountBtn;
    CircleImageView imageView;
    FirebaseAuth auth;
    String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    Uri imageUri;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        registername = findViewById(R.id.registername);
        imageView = findViewById(R.id.profile_image);
        registeremail = findViewById(R.id.registeremail);
        registerpassword = findViewById(R.id.registerpassword);
        registerconfirmpassword = findViewById(R.id.registerconfirmpassword);
        registerUserBtn=findViewById(R.id.Signupbutton);
        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
             String name=registername.getText().toString();
                String email=registeremail.getText().toString();
                String password=registerpassword.getText().toString();
                String confirmpassword=registerconfirmpassword.getText().toString();
                String status="Hey there I'm Using this App";

                  if(TextUtils.isEmpty(name)||TextUtils.isEmpty(email)||
                          TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmpassword))

                  {
                      progressDialog.dismiss();
                      Toast.makeText(Register.this,"Please Enter the valid data",Toast.LENGTH_SHORT).show();
                  }else if(!email.matches(email_pattern))
                  {
                      registeremail.setError("please enter valid email");

                      progressDialog.dismiss();
                      Toast.makeText(Register.this,"Please Enter the valid Email",Toast.LENGTH_SHORT).show();
                  }else if(!password.equals(confirmpassword))
                  {
                      progressDialog.dismiss();
                      Toast.makeText(Register.this,"Password does not match",Toast.LENGTH_SHORT).show();
                  }else if(password.length()<6)
                  {
                      progressDialog.dismiss();
                      Toast.makeText(Register.this,"Password more than 6 numbers",Toast.LENGTH_SHORT).show();

                  }else{
                      auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {

                          if(task.isSuccessful())
                          {

                             DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                             StorageReference storageReference=storage.getReference().child("uplod").child(auth.getUid());
                             if (imageUri!=null)
                             {
                                 storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                         if (task.isSuccessful())
                                         {
                                             storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                 @Override
                                                 public void onSuccess(Uri uri) {
                                                    imageURI= uri.toString();
                                                    Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                startActivity(new Intent(Register.this, MainActivity.class));
                                                            }else {
                                                                Toast.makeText(Register.this,"Eroor in creating user",Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                                 }
                                             });
                                         }

                                     }
                                 });


                             }else {
                                 String status="Hey there I'm Using this App";

                                 imageURI= "https://firebasestorage.googleapis.com/v0/b/quick-app-bcb85.appspot.com/o/profile_image.png?alt=media&token=fe692321-a8a9-4159-bb43-14064bec624f";
                                 Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                 reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful())
                                         {
                                             progressDialog.dismiss();
                                             startActivity(new Intent(Register.this,MainActivity.class));
                                         }else {
                                             Toast.makeText(Register.this,"Error in creating user",Toast.LENGTH_SHORT).show();
                                         }

                                     }
                                 });
                             }

                          }else{
                              progressDialog.dismiss();

                              Toast.makeText(Register.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                          }

                      }
                  });

                  }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        createAccountBtn = findViewById(R.id.regiserlogin2);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, login.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                imageUri=data.getData();
                imageView.setImageURI(imageUri);
            }
        }

    }
}



