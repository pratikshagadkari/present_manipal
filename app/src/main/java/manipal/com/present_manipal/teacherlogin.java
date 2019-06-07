package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class teacherlogin extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    EditText a, b;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlogin);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        a = findViewById(R.id.login_email);
        b = findViewById(R.id.login_pass);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        user = auth.getCurrentUser();
    }

    public void mainpage_go(View view) {
        if (user != null) {
            Toast.makeText(this, "User already Logged in", Toast.LENGTH_SHORT).show();
        }
        String email = a.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email Not Provided",Toast.LENGTH_SHORT).show();
            return;}
        String pass = b.getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Password Not Provided",Toast.LENGTH_SHORT).show();
            return;}
            progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(teacherlogin.this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    database.getReference().child("Teachers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(teacherlogin.this, "Log In Successfull", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(teacherlogin.this, teacher_mainpage.class);
                                startActivity(i);
                                finish();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                Toast.makeText(teacherlogin.this,"Wrong User Type",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(teacherlogin.this,choice_page.class);
                                startActivity(i);
                                auth.signOut();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else
                    Toast.makeText(teacherlogin.this, "Entered Details Is Not Correct", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void reg_teach(View view) {
        Intent i = new Intent(teacherlogin.this, teacher_registration.class);
        startActivity(i);
    }

    public void change1(View view) {
        Intent i=new Intent(teacherlogin.this,forgot_password_teacher.class);
        startActivity(i);
    }
}
