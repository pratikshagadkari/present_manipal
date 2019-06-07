package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class teacherlogin extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    EditText a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlogin);
        auth = FirebaseAuth.getInstance();
        a = findViewById(R.id.login_email);
        b = findViewById(R.id.login_pass);
        user = auth.getCurrentUser();
    }

    public void mainpage_go(View view) {
        if (user != null) {
            Toast.makeText(this, "User already Logged in", Toast.LENGTH_SHORT).show();
        }
        String email = a.getText().toString().trim();
        String pass = b.getText().toString().trim();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(teacherlogin.this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(teacherlogin.this, "Log In Successfull", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(teacherlogin.this, student_mainpage.class);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(teacherlogin.this, "Entered Details Is Not Correct", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void reg_teach(View view) {
        Intent i = new Intent(teacherlogin.this, teacher_registration.class);
        startActivity(i);
    }
}
