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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class string_receive extends AppCompatActivity {
    Button but,but1;
    TextView txt;
    EditText txt1;
    DatabaseReference ref;
    FirebaseDatabase data;
    FirebaseAuth auth;
    static String location;
    int location_mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_receive);
        data=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        ref=data.getReference();
        txt=findViewById(R.id.notif);
        txt1=findViewById(R.id.upload);
    }
    public void location_fix(View view) throws Settings.SettingNotFoundException {
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
                                    string_receive.this.startActivity(intent);
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
            location_student abc=new location_student(string_receive.this);
            abc.main();

        }
    }

    public void refresh(View view) {
        ref.child("Students").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_details abc=dataSnapshot.getValue(user_details.class);
                final String regd=abc.regd_no;
                ref.child("Notifications").child(regd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String s=dataSnapshot.child("uid").getValue().toString();
                            String s1="<u>"+s+"</u";
                            String s2=dataSnapshot.child("location").getValue().toString();
                            Log.d("locat22",s2);
                            txt.setText(Html.fromHtml(s1));
                            txt.setTextColor(Color.parseColor("#000000"));
                        }
                        else {
                            Toast.makeText(string_receive.this,"Your Unique Code Does Not Exist",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void abc(View view) {
        if(location!=null){
            final String s=txt1.getText().toString();
            ref.child("Students").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user_details abc=dataSnapshot.getValue(user_details.class);
                    final String regd=abc.regd_no;
                    final String add=abc.add_details;
                    ref.child("Notifications").child(regd).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String s1=dataSnapshot.child("uid").getValue().toString();
                                String s2=dataSnapshot.child("location").getValue().toString();
                                Log.d("locat22",s2);
                                String s3=dataSnapshot.child("subject").getValue().toString();
                                String s4=dataSnapshot.child("time").getValue().toString();
                                if(s.equals(s1)&&location!=null) {
                                    if(s2.equals("empty_empty")){
                                        Toast.makeText(string_receive.this,"Gps Location was not used in the last Attendance",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Double lat1 = 0.00,lat2=0.00,long1=0.00,long2=0.00;
                                        for(int i=0;i<s2.length();i++)
                                        {
                                            if(s2.charAt(i)==','){
                                                lat1=Double.parseDouble(s2.substring(0,i));
                                                long1=Double.parseDouble(s2.substring(i+1,s2.length()));
                                                Log.d("loctn1",lat1.toString());
                                                Log.d("loctn2",long1.toString());
                                            }
                                        }
                                        for(int i=0;i<location.length();i++)
                                        {
                                            if(location.charAt(i)==','){
                                                lat2=Double.parseDouble(location.substring(0,i));
                                                long2=Double.parseDouble(location.substring(i+1,location.length()));
                                                Log.d("loctn3",lat2.toString());
                                                Log.d("loctn4",long2.toString());
                                            }
                                        }
                                        DistanceCalculator ds=new DistanceCalculator();
                                        double dist=ds.distance(lat1,long1,lat2,long2);
                                        Log.d("loctn5",String.valueOf(lat1));
                                        Log.d("loctn5",String.valueOf(long1));
                                        Log.d("loctn5",String.valueOf(lat2));
                                        Log.d("loctn5",String.valueOf(long2));
                                        Log.d("loctn5",String.valueOf(dist));
                                        if(dist>75.0){
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                                            String strDate = mdformat.format(calendar.getTime());
                                            data.getReference().child("Attendance").child(add.substring(0,add.length()-1)).child(add.substring(add.length()-1,add.length())).child(s3).child(strDate).child(s4).child(regd).setValue("Absent");
                                        }
                                        else{
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                                            String strDate = mdformat.format(calendar.getTime());
                                            data.getReference().child("Attendance").child(add.substring(0,add.length()-1)).child(add.substring(add.length()-1,add.length())).child(s3).child(strDate).child(s4).child(regd).setValue("Present");
                                        }
                                        data.getReference().child("Notifications").child(regd).removeValue();
                                    }
                                }
                                txt.setTextColor(Color.parseColor("#000000"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Intent i=new Intent(string_receive.this,student_mainpage.class);
        startActivity(i);
        finish();
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
    private void golocation() {
        location_student abc=new location_student(this);
        abc.main();
    }
}
