package manipal.com.present_manipal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contact_students extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase data;
    DatabaseReference ref;
    FirebaseAuth auth;
    TextView txt;
    Switch toggleButton;
    contactadapter adapter;
    ImageButton but,but1;
    ArrayList<contacts_array> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_students);
        but=findViewById(R.id.go_call);
        but1=findViewById(R.id.go_email);
        recyclerView=findViewById(R.id.recycle);
        data=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        ref=data.getReference();
        list=new ArrayList<>();
        ref.child("Teachers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                  if(!dsp.child("add_details").getValue().toString().equals("empty")){
                      String name="",phone="",email="";
                      name=name+dsp.child("uname").getValue().toString();
                      email=email+dsp.child("personal_email_id").getValue().toString();
                      phone=phone+dsp.child("phone_no").getValue().toString();
                      Log.d("xyz",name);
                      Log.d("xyz",email);
                      Log.d("xyz",phone);
                      list.add(new contacts_array(name,email,phone));
                      LinearLayoutManager layoutManager=new LinearLayoutManager(contact_students.this);
                      RecyclerView.LayoutManager layoutManager1=layoutManager;
                      recyclerView.setLayoutManager(layoutManager1);
                      adapter=new contactadapter(contact_students.this,list);
                      recyclerView.setAdapter(adapter);
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
        super.onBackPressed();
    }
}
