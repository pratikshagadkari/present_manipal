package manipal.com.present_manipal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class display_det_att extends AppCompatActivity {
    String regd,subj;
    FirebaseAuth auth;
    String details;
    String s="ram";
    TextView txt;
    FirebaseDatabase data;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageRef,image_path ;
    DatabaseReference ref,ref1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_det_att);
        txt=findViewById(R.id.disp);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            details=getIntent().getStringExtra("add_details");
            regd=getIntent().getStringExtra("regd");
            subj=getIntent().getStringExtra("sub");
        }
        Log.d("qwerty",regd);
        auth=FirebaseAuth.getInstance();
        data= FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();
        ref=data.getReference();
        storage=FirebaseStorage.getInstance();
        final String abc=details.substring(0,details.length()-1);
        final String abc1=details.substring(abc.length(),details.length());
        Log.d("message",abc);
        Log.d("message",abc1);
        Log.d("message",subj);
        data.getReference().child("Attendance").child(abc).child(abc1).child(subj).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp:dataSnapshot.getChildren()){
                    String s=dsp.getKey();
                    Log.d("abcde",s);
                    txt.append(" "+s+" ");
                    for(DataSnapshot dsp1:dsp.getChildren()){
                        txt.append(" "+dsp1.getKey()+" ");
                        for (DataSnapshot dsp2:dsp1.getChildren()){
                            Log.d("message2",dsp2.getValue().toString());
                            if(dsp2.getKey().equals(regd)){
                                Log.d("message2",dsp2.getValue().toString());
                                txt.append(" "+dsp2.getValue().toString()+" ");
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
