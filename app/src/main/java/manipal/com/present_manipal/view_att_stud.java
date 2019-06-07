package manipal.com.present_manipal;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.opencensus.internal.StringUtil;

public class view_att_stud extends AppCompatActivity {
    FirebaseDatabase data;
    FirebaseAuth auth;
    DatabaseReference ref;
    String details,regd;
    TextView txt1;
    Dialog mydialog;
    Button but;
    String a="",b="",c="",d="",e="";
    RecyclerView recyclerView;
    ArrayList<attendance_array> list;
    TextView x,y,z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_att_stud);
        data=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        ref=data.getReference();
        recyclerView=findViewById(R.id.recycle);
        list=new ArrayList<>();
        mydialog=new Dialog(this);
        String asd=auth.getUid();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            regd = extras.getString("regd_no");
            Log.d("xyz",regd);
        }
        data.getReference().child("Students").child(asd).child("add_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                details= dataSnapshot.getValue().toString();
                final String abc=details.substring(0,details.length()-1);
                final String abc1=details.substring(abc.length(),details.length());
                data.getReference().child("Subject_List").child(abc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(final DataSnapshot child : dataSnapshot.getChildren() ){
                            final String s=child.getValue().toString();
                            data.getReference().child("Attendance").child(abc).child(abc1).child(child.getValue().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    double count_present = 0.00, count_absent = 0.00,total_class=0,percentage=0;
                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {//date
                                            Log.d("snap", dsp.getValue().toString());
                                            for (DataSnapshot dsp1 : dsp.getChildren()) {//hour
                                                Log.d("snap1", dsp1.getValue().toString());
                                                for (DataSnapshot dsp2 : dsp1.getChildren()) {//regd
                                                    if (dsp2.getKey().equals(regd)) {
                                                        Log.d("snap2", dsp2.getValue().toString());
                                                        if (dsp2.getValue().toString().equals("Present"))
                                                            count_present++;
                                                        else
                                                            count_absent++;
                                                    }

                                                }
                                            }
                                        }
                                        total_class=count_absent+count_present;
                                        percentage=0.00;
                                        try {
                                            percentage= (count_present * 100 / total_class);
                                            if(Double.isNaN(percentage))
                                                percentage=0.00;
                                            DecimalFormat numberFormat = new DecimalFormat("#.00");
                                            percentage= Double.parseDouble(numberFormat.format(percentage));
                                        }
                                        catch (Exception e){
                                            Toast.makeText(view_att_stud.this,"Error in Retrieveing Data",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    list.add(new attendance_array(s,count_present,total_class,percentage,a,b,c,s,e));
                                    LinearLayoutManager layoutManager=new LinearLayoutManager(view_att_stud.this);
                                    RecyclerView.LayoutManager layoutManager1=layoutManager;
                                    recyclerView.setLayoutManager(layoutManager1);
                                    second_att_adapter adapter=new second_att_adapter(view_att_stud.this,list);
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

    @Override
    public void onBackPressed() {
        mydialog.dismiss();
        super.onBackPressed();
    }
}
