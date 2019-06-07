package manipal.com.present_manipal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class update_info_students extends AppCompatActivity {
    FirebaseDatabase data;
    DatePickerDialog.OnDateSetListener datelistener;
    FirebaseAuth auth;
    DatabaseReference ref;
    String s1="";
    Spinner spin,spin2,spin3;
    EditText et1,et2,et3;
    Button but;
    TextView branch,email,semester,section,number;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        data=FirebaseDatabase.getInstance();
        et1=findViewById(R.id.email);
        //but=findViewById(R.id.inputdob);
        et2=findViewById(R.id.phone);
        auth=FirebaseAuth.getInstance();
        ref=data.getReference().child("Students").child(auth.getUid());
        spin=findViewById(R.id.br_sem);
        spin2=findViewById(R.id.sem_spinner);
        spin3=findViewById(R.id.sec_spinner);
        branch=findViewById(R.id.view_branch);
        section=findViewById(R.id.view_section);
        semester=findViewById(R.id.view_semester);
        email=findViewById(R.id.view_email);
        number=findViewById(R.id.view_phone);

        linearLayout=findViewById(R.id.linearLayout);
        data.getReference().child("Students").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("add_details").getValue().toString().equals("empty")){
                    String s=dataSnapshot.child("add_details").getValue().toString();
                    String sec= String.valueOf(s.charAt(s.length()-1));
                    String sem= String.valueOf(s.charAt(s.length()-2));
                    String br=s.substring(0,s.length()-2);
                    section.setText("Section "+sec);
                    semester.setText("Semester "+sem);
                    branch.setText("Branch "+br);
                    email.setText("Email "+dataSnapshot.child("personal_email_id").getValue().toString());
                    number.setText("Number "+dataSnapshot.child("phone_no").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void update(View view) {
        String a=spin.getSelectedItem().toString();
        String b=spin2.getSelectedItem().toString();
        String c=spin3.getSelectedItem().toString();
        String ab=a+b+c;
        String set1=""+et1.getText().toString();
        String set2=""+et2.getText().toString();
        ref.child("add_details").setValue(ab);
        ref.child("phone_no").setValue(set2);
        ref.child("personal_email_id").setValue(set1);
        //ref.child("dob").setValue(s1);
        Log.d("yolo",s1);
        Intent i=new Intent(update_info_students.this,student_mainpage.class);
        Toast.makeText(update_info_students.this,"Details Updated",Toast.LENGTH_SHORT).show();
        startActivity(i);
        finish();
    }

    /*public void dob(View view) {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int date=cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog picker = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            picker = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,datelistener,year,month,date);
            picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));picker.setCancelable(false);
            picker.show();
        }
        datelistener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month+=1;
                String s=day+"/"+month+"/"+year;
                s1=s1+day+"-"+month+"-"+year;
                Log.d("dob",s);
                but.setText(s);
            }
        };
    }*/
}
