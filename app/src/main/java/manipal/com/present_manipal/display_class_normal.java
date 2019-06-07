package manipal.com.present_manipal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class display_class_normal extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase data;
    FirebaseUser user;
    DatabaseReference ref;
    String s, s4;
    List<String> lst = new ArrayList<String>();
    List<String> lst1 = new ArrayList<String>();
    TextView subject_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class_normal);
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        String s1 = classroom_details.branch;
        String s2 = classroom_details.year;
        String s3 = classroom_details.section;
        String sub_text=classroom_details.subject;
        subject_text=findViewById(R.id.subject);
        subject_text.setText(sub_text);
        s = s1 + s2 + s3;
        Log.d("message",s);
        s4 = s1 + s2;
        Log.d("message",s4);
        data.getReference().child("Students").orderByChild("add_details").equalTo(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    lst.add(dsp.child("regd_no").getValue().toString());
                    lst1.add(dsp.child("uname").getValue().toString());
                }
                for (int i = 0; i < lst.size(); i++) {
                    String names = lst.get(i)+"\t\t"+lst1.get(i);
                    LinearLayout l1 = findViewById(R.id.scroll);
                    CheckBox ch = new CheckBox(display_class_normal.this);
                    float v1 = 20;
                    ch.setTextSize(v1);
                    ch.setButtonTintList(getResources().getColorStateList(R.color.colorAccent));
                    ch.setText(names);
                    ch.setId(i);
                    ch.setTextColor(Color.parseColor("#000000"));
                    l1.addView(ch);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Submit_attendance(View view) {
        Spinner spin = findViewById(R.id.time_spin);
        final String spin1 = spin.getSelectedItem().toString();
        if (spin1.equals("Select Time"))
            Toast.makeText(display_class_normal.this, "Please Select a Time", Toast.LENGTH_SHORT).show();
        else {
            int i;
            for (i = 0; i < lst.size(); i++) {
                CheckBox ch = findViewById(i);
                if (ch.isChecked()) {
                    final random_string_generate ob = new random_string_generate();
                    final String s = ob.generate_string();
                    final String s1 = lst.get(i);
                    Query q = data.getReference().child("Students").orderByChild("regd_no").equalTo(lst.get(i));
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String location="empty_empty";
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                                String strDate = mdformat.format(calendar.getTime());
                                String text = "Present";
                                classroom_details abc = new classroom_details();
                                String subjec = abc.subject;
                                update_att upd = new update_att(strDate, text, subjec);
                                classroom_details abcd = new classroom_details();
                                String sub = abcd.subject;
                                String sec = abcd.section;
                                attendance_status object = new attendance_status(s1, text);
                                data.getReference().child("Attendance").child(s4).child(sec).child(sub).child(upd.strDate).child(spin1).child(s1).setValue(text);
                                data.getReference().child("Notifications").child(s1).child("location").setValue(location);
                                data.getReference().child("Notifications").child(s1).child("uid").setValue(s);
                                data.getReference().child("Notifications").child(s1).child("time").setValue(spin1);
                                data.getReference().child("Notifications").child(s1).child("subject").setValue(sub);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    final String s2 = lst.get(i);
                    final String location="empty_empty";
                    Query q = data.getReference().child("Students").orderByChild("regd_no").equalTo(lst.get(i));
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                                String strDate = mdformat.format(calendar.getTime());
                                String text = "Absent";
                                classroom_details abc = new classroom_details();
                                String subjec = abc.subject;
                                update_att upd = new update_att(strDate, text, subjec);
                                classroom_details abcd = new classroom_details();
                                String sub = abcd.subject;
                                String sec = abcd.section;
                                data.getReference().child("Attendance").child(s4).child(sec).child(sub).child(upd.strDate).child(spin1).child(s2).setValue(text);
                                data.getReference().child("Notifications").child(s2).child("location").setValue(location);
                                data.getReference().child("Notifications").child(s2).child("uid").setValue(s);
                                data.getReference().child("Notifications").child(s2).child("time").setValue(spin1);
                                data.getReference().child("Notifications").child(s2).child("subject").setValue(sub);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            Toast.makeText(display_class_normal.this, "Submitting Attendance", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(display_class_normal.this, teacher_mainpage.class);
            Toast.makeText(display_class_normal.this, "Attendance Updated", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
    }
}
