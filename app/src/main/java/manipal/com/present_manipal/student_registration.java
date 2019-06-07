package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class    student_registration extends AppCompatActivity {
    FirebaseDatabase data;
    FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseUser user;
    ImageView eye_pass,eye_con_pass;
    ProgressBar progressBar;
    EditText a, b, c, d, con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        a = findViewById(R.id.stud_name);
        b = findViewById(R.id.stud_email);
        //eye_pass=findViewById(R.id.eye_pass);
        //eye_con_pass=findViewById(R.id.eye_con_pass);
        b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(s.toString())&&s.toString().trim().length()==1){
                    Toast.makeText(student_registration.this,"Enter College Email Id",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        c = findViewById(R.id.stud_pass);
        c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(s.toString())&&s.toString().trim().length()==1){
                    Toast.makeText(student_registration.this,"Password Length Should Be Greater Than 6",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        con=findViewById(R.id.confirm_pass);
        c.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        con.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        d=findViewById(R.id.stud_regd);

        /*eye_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c.getText().toString()=="SHOW"){

                }
            }
        });*/
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference();

        b.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    public void register(View view) {

        final String name = a.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Name Not Provided",Toast.LENGTH_SHORT).show();
            return;}
        final String email = b.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email Not Provided",Toast.LENGTH_SHORT).show();
            return;}
        final String pass = c.getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Password Not Provided",Toast.LENGTH_SHORT).show();
            return;}
            if(pass.length()<6){
                Toast.makeText(this,"Password Length Should Be Greater Than 6",Toast.LENGTH_SHORT).show();
                return;}
        final String registration=d.getText().toString();
        if(TextUtils.isEmpty(registration)){
            Toast.makeText(this,"Registration Number Not Provided",Toast.LENGTH_SHORT).show();
            return;}
        final String con_pass=con.getText().toString();
        if(!pass.equals(con_pass)){
            Toast.makeText(this,"Passwords do not Match",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(student_registration.this,"Please wait we are trying to connect to the servers",Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        final String type="student";
        final String first_time="empty";
        int i=email.lastIndexOf("@");
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().sendEmailVerification();
                    Toast.makeText(student_registration.this, "Registration Successful Please Verify Email and Login", Toast.LENGTH_SHORT).show();
                    user_details user = new user_details(name, email, registration,type,first_time);
                    ref.child("Students").child(auth.getUid()).setValue(user);
                    auth.signOut();
                    Intent i = new Intent(student_registration.this, studentlogin.class);
                    startActivity(i);
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                } else
                    Toast.makeText(student_registration.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
