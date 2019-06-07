package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class student_registration extends AppCompatActivity {
    FirebaseDatabase data;
    FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseUser user;
    EditText a, b, c,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        a = findViewById(R.id.stud_name);
        b = findViewById(R.id.stud_email);
        c = findViewById(R.id.stud_pass);
        d=findViewById(R.id.stud_regd);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference();
    }
    public void register(View view) {
        Toast.makeText(student_registration.this,"Please wait we are trying to connect to the servers",Toast.LENGTH_SHORT).show();
        final String name = a.getText().toString().trim();
        final String email = b.getText().toString().trim();
        final String pass = c.getText().toString().trim();
        final String registration=d.getText().toString();
        int i=email.lastIndexOf("@");
        if(email.substring(i+1,email.length()).compareTo("muj.manipal.edu")==0) {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().sendEmailVerification();
                        Toast.makeText(student_registration.this, "Registration Successful Please Verify Email and Login", Toast.LENGTH_SHORT).show();
                        user_details user = new user_details(name, email, registration);
                        ref.child("Students").child(auth.getUid()).setValue(user);
                        Intent i = new Intent(student_registration.this, student_mainpage.class);
                        startActivity(i);
                    } else
                        Toast.makeText(student_registration.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(student_registration.this,"Please Enter Email Id Provided By College",Toast.LENGTH_SHORT).show();
    }
}
