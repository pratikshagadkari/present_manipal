package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class view_attendance_teacher extends AppCompatActivity {
    Spinner spin,spin2,spin3,spin4;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_teacher);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        spin = findViewById(R.id.branch);
        spin2 = findViewById(R.id.year);
        spin3 = findViewById(R.id.subject);
        spin4 = findViewById(R.id.section);
        final String[] branch = new String[1];
        final String[] year = new String[1];
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch[0] =spin.getSelectedItem().toString();
                year[0] =spin2.getSelectedItem().toString();
                if(branch[0]==null)
                    Toast.makeText(view_attendance_teacher.this,"Fill out Branch",Toast.LENGTH_SHORT).show();
                else if (year[0]==null)
                    Toast.makeText(view_attendance_teacher.this,"Fill out Year",Toast.LENGTH_SHORT).show();
                final ArrayList<String> sublist = new ArrayList<>();
                sublist.add("Select Subject");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Subject_List").child((branch[0] + year[0]));
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                        for (Iterator<DataSnapshot> it = iterable; it.hasNext(); ) {
                            DataSnapshot dataSnapshot1 = it.next();
                            sublist.add(dataSnapshot1.getValue(String.class));}
                        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(view_attendance_teacher.this, android.R.layout.simple_spinner_item, sublist);
                        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin3.setAdapter(subAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch[0] =spin.getSelectedItem().toString();
                year[0] =spin2.getSelectedItem().toString();
                final ArrayList<String> sublist = new ArrayList<>();
                sublist.add("Select Subject");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Subject_List").child((branch[0] + year[0]));
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                        for (Iterator<DataSnapshot> it = iterable; it.hasNext(); ) {
                            DataSnapshot dataSnapshot1 = it.next();
                            sublist.add(dataSnapshot1.getValue(String.class));}
                        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(view_attendance_teacher.this, R.layout.spinner_colour, sublist);
                        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin3.setAdapter(subAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void view_class(View view) {
        final String a,b,c,d;
        a=spin.getSelectedItem().toString();
        if(a.equals("Select Branch")){
            Toast.makeText(this,"Select Branch",Toast.LENGTH_SHORT).show();
            return;
        }
        b=spin2.getSelectedItem().toString();
        if(b.equals("Select Semester")){
            Toast.makeText(this,"Select Semester",Toast.LENGTH_SHORT).show();
            return;
        }
        c=spin3.getSelectedItem().toString();
        if(c.equals("Select Section")){
            Toast.makeText(this,"Select Section",Toast.LENGTH_SHORT).show();
            return;
        }
        d=spin4.getSelectedItem().toString();
        if(d.equals("Select Subject")){
            Toast.makeText(this,"Select Subject",Toast.LENGTH_SHORT).show();
            return;
        }
        classroom_details det=new classroom_details(a,b,c,d);
        Intent i=new Intent(this,display_class_attendance.class);
        i.putExtra("branch",a);
        i.putExtra("semester",b);
        i.putExtra("section",c);
        i.putExtra("subject",d);
        startActivity(i);
    }
}
