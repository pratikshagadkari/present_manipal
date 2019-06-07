package manipal.com.present_manipal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class teacher_registration extends AppCompatActivity {
    FirebaseAuth auth;
    EditText a, b, c, d, con;
    String m_Text="";
    ImageView eye_pass,eye_con_pass;
    ProgressBar progressBar;
    FirebaseDatabase data;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        a = findViewById(R.id.teach_name);
        b = findViewById(R.id.teach_email);
        b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(s.toString())&&s.toString().trim().length()==1){
                    Toast.makeText(teacher_registration.this,"Enter Email Id",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        c = findViewById(R.id.teach_pass);
        c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(s.toString())&&s.toString().trim().length()==1){
                    Toast.makeText(teacher_registration.this,"Password Length Should Be Greater Than 6",Toast.LENGTH_SHORT).show();
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
        d = findViewById(R.id.teach_id);
        auth=FirebaseAuth.getInstance();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference();
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
            return;
        }
        final String con_pass=con.getText().toString();
        if(!pass.equals(con_pass)){
            Toast.makeText(this,"Passwords do not Match",Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("Input Key");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setCancelable(false);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(m_Text.equals("abcdef")){
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(teacher_registration.this,"Please wait we are trying to connect to the servers",Toast.LENGTH_SHORT).show();
                    final String type="teacher";
                    final String first_time="empty";
                    int i=email.lastIndexOf("@");

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth.getCurrentUser().sendEmailVerification();
                                Toast.makeText(teacher_registration.this, "Registration Successful Please Verify Email and Login", Toast.LENGTH_SHORT).show();
                                user_details user = new user_details(name, email, registration,type,first_time);
                                ref.child("Teachers").child(auth.getUid()).setValue(user);
                                Intent i = new Intent(teacher_registration.this, teacherlogin.class);
                                auth.signOut();
                                startActivity(i);
                                finish();
                                progressBar.setVisibility(View.INVISIBLE);
                            } else
                                Toast.makeText(teacher_registration.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else
                    Toast.makeText(teacher_registration.this,"Incorrect Key",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(teacher_registration.this,"Key not Provided",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.show();



    }
}