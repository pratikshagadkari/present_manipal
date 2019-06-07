package manipal.com.present_manipal;

        import android.Manifest;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.provider.Settings;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

public class display_class extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase data;
    FirebaseUser user;
    DatabaseReference ref;
    List<String> lst = new ArrayList<String>();
    List<String> lst1 = new ArrayList<String>();
    String s,s4,longitude,latitude,subject;
    int location_mode=0;
    TextView subject_text;
    static String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class);
        auth=FirebaseAuth.getInstance();
        data= FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();
        String s1=classroom_details.branch;
        String s2=classroom_details.year;
        String s3=classroom_details.section;
        String sub_text=classroom_details.subject;
        subject_text=findViewById(R.id.subject);
        subject_text.setText(sub_text);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            longitude = extras.getString("longitude");
            latitude = extras.getString("latitude");
            subject=extras.getString("subject");
        }
        s=s1+s2+s3;
        s4=s1+s2;
        data.getReference().child("Students").orderByChild("add_details").equalTo(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                    lst.add(dsp.child("regd_no").getValue().toString());
                    lst1.add(dsp.child("uname").getValue().toString());
                }
                for(int i=0;i<lst.size();i++)
                {
                    String names=lst1.get(i)+"\t\t"+lst.get(i);
                    LinearLayout l1=findViewById(R.id.scroll);
                    CheckBox ch=new CheckBox(display_class.this);
                    float v1=20;
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
        Spinner spin=findViewById(R.id.time_spin);
        final String spin1=spin.getSelectedItem().toString();
        if(spin1.equals("Select Time")||(location==null)){
             if(location==null)
                Toast.makeText(display_class.this,"Fix Location!",Toast.LENGTH_SHORT).show();
             else if(spin1.equals("Select Time"))
                Toast.makeText(display_class.this,"Please Select a Time",Toast.LENGTH_SHORT).show();
        }
        else{
            int i;
            for(i=0;i<lst.size();i++)
            {
                CheckBox ch=findViewById(i);
                if(ch.isChecked())
                {
                    final random_string_generate ob = new random_string_generate();
                    final String s = ob.generate_string();
                    final String s1=lst.get(i);
                    Query q=data.getReference().child("Students").orderByChild("regd_no").equalTo(lst.get(i));
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Calendar calendar = Calendar.getInstance();
                                Date time=calendar.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                String formattedDate=dateFormat. format(time);
                                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                                String strDate = mdformat.format(calendar.getTime());
                                String text = "Pending";
                                classroom_details abc = new classroom_details();
                                String subjec = abc.subject;
                                update_att upd = new update_att(strDate, text, subjec);
                                classroom_details abcd = new classroom_details();
                                String sub = abcd.subject;
                                String sec = abcd.section;
                                attendance_status object=new attendance_status(s1,text);
                                data.getReference().child("Attendance").child(s4).child(sec).child(sub).child(upd.strDate).child(spin1).child(s1).setValue(text);
                                data.getReference().child("Notifications").child(s1).child("location").setValue(location);
                                data.getReference().child("Notifications").child(s1).child("uid").setValue(s);
                                data.getReference().child("Notifications").child(s1).child("time").setValue(spin1);
                                data.getReference().child("Notifications").child(s1).child("subject").setValue(sub);
                                data.getReference().child("Notifications").child(s1).child("counter").setValue(formattedDate);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }
                else{
                    final String s2=lst.get(i);
                    Query q=data.getReference().child("Students").orderByChild("regd_no").equalTo(lst.get(i));
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                                String strDate = mdformat.format(calendar.getTime());
                                String text="Absent";
                                classroom_details abc=new classroom_details();
                                String subjec=abc.subject;
                                update_att upd=new update_att(strDate,text,subjec);
                                classroom_details abcd=new classroom_details();
                                String sub=abcd.subject;
                                String sec=abcd.section;
                                data.getReference().child("Attendance").child(s4).child(sec).child(sub).child(upd.strDate).child(spin1).child(s2).setValue(text);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            Toast.makeText(display_class.this,"Submitting Attendance",Toast.LENGTH_SHORT).show();
            Toast.makeText(display_class.this,"Attendance Updated",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(display_class.this,teacher_mainpage.class);
            startActivity(intent);
            finish();
        }
    }

    public void location(View view) throws Settings.SettingNotFoundException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
            return;
        }
        location_mode=Settings.Secure.getInt(this.getContentResolver(),Settings.Secure.LOCATION_MODE);
        if(location_mode==Settings.Secure.LOCATION_MODE_OFF) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(
                    "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                    .setTitle("Location Service Disabled")
                    .setCancelable(false)
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    display_class.this.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
            builder.create();
            builder.show();
        }
        else {
            Toast.makeText(this, "Getting Your Location!", Toast.LENGTH_LONG).show();
            golocation();
        }
    }

    private void golocation() {
        location_teacher abc=new location_teacher(this);
        abc.main();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    golocation();
                }
        }
    }
}
