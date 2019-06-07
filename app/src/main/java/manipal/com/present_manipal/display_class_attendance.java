package manipal.com.present_manipal;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class display_class_attendance extends AppCompatActivity {
    Dialog mydialog;
    LinearLayout linearLayout;
    TextView x,y,z;
    FirebaseDatabase data;
    ImageButton but1,but2;
    ArrayList<attendance_array>list;
    FirebaseAuth auth;
    Button but;
    RecyclerView recyclerView;
    String branch,semester,section,subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class_att);
        mydialog=new Dialog(this);
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.recycle);
        Bundle extras = getIntent().getExtras();
        String s1=classroom_details.branch;
        String s2=classroom_details.year;
        final String s3=classroom_details.section;
        final String s4=classroom_details.subject;
        Log.d("stray",s1);
        Log.d("stray",s2);
        Log.d("stray",s3);
        Log.d("stray",s4);
        if (extras != null) {
            branch = extras.getString("branch");
            semester = extras.getString("semester");
            section=extras.getString("section");
            subject=extras.getString("subject");

        }
        data=FirebaseDatabase.getInstance();
        final String s=s1+s2;
        Log.d("user",s);
        final String lol=s1+s2+s3;
        data.getReference().child("Students").orderByChild("add_details").equalTo(lol).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                    if(dsp.exists()){
                        int c=0;
                        final String name=dsp.child("uname").getValue().toString();
                        //Log.d("data2",dsp.child("regd_no").getValue().toString());
                        final String regd=dsp.child("regd_no").getValue().toString();
                        final String no=dsp.child("phone_no").getValue().toString();
                        final String number="tel:"+no;
                        final String email=dsp.child("personal_email_id").getValue().toString();
                        final String to[]={email};
                        Log.d("regd",regd);

                        data.getReference().child("Attendance").child(s).child(s3).child(s4).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dsp) {
                                if(dsp.exists()) {
                                    int countabsent = 0, countpresent = 0;
                                    for (DataSnapshot dataSnapshot1 : dsp.getChildren()) {
                                        Log.d("message", dsp.getValue().toString());
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            Log.d("message", dataSnapshot2.getValue().toString());
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Log.d("message", dataSnapshot3.getValue().toString());
                                                if (dataSnapshot3.getKey().equals(regd)) {
                                                    if (dataSnapshot3.getValue().toString().equals("Present"))
                                                        countpresent++;
                                                    else
                                                        countabsent++;
                                                }
                                            }
                                        }
                                    }
                                    double total_class=countabsent+countpresent;
                                    double percentage=0.00;
                                    try {
                                        percentage= (countpresent * 100 / total_class);
                                        DecimalFormat numberFormat = new DecimalFormat("#.00");
                                        percentage= Double.parseDouble(numberFormat.format(percentage));
                                    }
                                    catch (Exception e){
                                        Toast.makeText(display_class_attendance.this,"Error in Retrieveing Data",Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d("name22",name);
                                    Log.d("name22", String.valueOf(countpresent));
                                    Log.d("name22", String.valueOf(total_class));
                                    Log.d("name22", String.valueOf(percentage));
                                    Log.d("message1",s4);
                                    list.add(new attendance_array(name,countpresent,total_class,percentage,no,email,regd,s4,lol));
                                    LinearLayoutManager layoutManager=new LinearLayoutManager(display_class_attendance.this);
                                    RecyclerView.LayoutManager layoutManager1=layoutManager;
                                    recyclerView.setLayoutManager(layoutManager1);
                                    attendanceadapter2 adapter=new attendanceadapter2(display_class_attendance.this,list);
                                    recyclerView.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        mydialog.dismiss();
        super.onBackPressed();
    }

    public void detailed(View view) {

    }
}
