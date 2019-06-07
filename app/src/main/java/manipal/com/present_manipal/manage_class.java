package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class manage_class extends AppCompatActivity {

    ArrayList<class_array> list;
    classadapter adapter;
    RecyclerView recyclerView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_class);

        list=new ArrayList<>();
        Intent e=getIntent();
        final String clas= e.getStringExtra("time");
        database=FirebaseDatabase.getInstance();
        //show=findViewById(R.id.show);
        database.getReference().child("Class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot3:dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot3.getChildren()){
                        if(dataSnapshot1.getKey().equals(clas)){
                            for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                if(dataSnapshot2.getValue().toString().equals("vacant")){
                                    String a=dataSnapshot2.getKey();
                                    String b=dataSnapshot1.getKey();
                                    String c=dataSnapshot3.getKey();
                                    list.add(new class_array(a,b,c));
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(manage_class.this);
        RecyclerView.LayoutManager layoutManager1=layoutManager;
        recyclerView.setLayoutManager(layoutManager1);
        adapter=new classadapter(manage_class.this,list);
        recyclerView.setAdapter(adapter);
    }
}
