package com.example.adminapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText nameedt, regedt, phoneedt,passwordedt;
    Button signupbtn;
    FirebaseAuth auth;
    FirebaseFirestore fstore;

    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        instance();
        SignUp();
    }
    void init()
    {
        nameedt=findViewById(R.id.userNameEDT);
        regedt=findViewById(R.id.userregEDT);
        phoneedt=findViewById(R.id.userPhoneEDT);
        passwordedt=findViewById(R.id.userPassEDT);
        signupbtn=findViewById(R.id.SignUpBtn);
        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneedt);
    }
    private void instance()
    {
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }
    private void SignUp()
    {
        signupbtn.setOnClickListener(view -> initateSignUp());
    }
    private void initateSignUp()
    {
        String name = nameedt.getText().toString();
        String mail = regedt.getText().toString();
        String phone = phoneedt.getText().toString();
        String password = passwordedt.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(this,"Name is empty", Toast.LENGTH_SHORT).show();
        }else if(mail.isEmpty()){
            Toast.makeText(this,"Email Address cannot be empty", Toast.LENGTH_SHORT).show();
        }else if(phone.isEmpty()){
            Toast.makeText(this,"Mobile number is empty", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this,"Password is empty", Toast.LENGTH_SHORT).show();
        }else if(!isValidEmail(mail)){
            Toast.makeText(this,"Email id is not valid", Toast.LENGTH_SHORT).show();
        }else if(!isValidName(name)){
            Toast.makeText(this,"Name is not valid", Toast.LENGTH_SHORT).show();
        }else  if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$") && !isValidPass(password)) {
            Toast.makeText(this, "Password must contain at least 8 charecters, one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long", Toast.LENGTH_LONG).show();
        }
        else if(!mail.endsWith("pict.edu"))
        {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Creating an account, Please wait...");
            progressDialog.show();

            auth.createUserWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignupActivity.this, "succesfully registered", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();
                    DocumentReference df = fstore.collection("Students").document(auth.getCurrentUser().getUid());
                    Map<String,Object> userInfo = new HashMap<>();


                    userInfo.put("Name",name);
                    userInfo.put("Email",mail);
                    userInfo.put("Password",password);
                    userInfo.put("Phone number",phone);
                    userInfo.put("isStudent",null);
                    userInfo.put("isTeacher","1");
                    userInfo.put("isHOD",null);

                    df.set(userInfo);
                    progressDialog.dismiss();
                    Intent intent=new Intent(SignupActivity.this,HomeActivity.class);

                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }


    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isValidPass(CharSequence target) {
        return (!TextUtils.isEmpty(target) && target.length()>=8);
    }
    public static boolean isValidPhone(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches() && target.length()==10);
    }
    public static boolean isValidName(CharSequence target) {
        return (!TextUtils.isEmpty(target) && target.length()>=2 );
    }
}
