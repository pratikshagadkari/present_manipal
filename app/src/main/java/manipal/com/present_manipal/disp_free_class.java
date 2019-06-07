package manipal.com.present_manipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class disp_free_class extends AppCompatActivity {
    Spinner spinner2;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_free_class);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        spinner2=findViewById(R.id.spinner2);
    }
    public void manage(View view) {

        if(spinner2.getSelectedItem().equals("Select Time")){
            Toast.makeText(this,"Please Select A Class",Toast.LENGTH_SHORT).show();
            return;
        }
        String s2 = String.valueOf(spinner2.getSelectedItem());
        Intent i =new Intent(disp_free_class.this,manage_class.class);
        i.putExtra("time",s2);
        startActivity(i);
    }

    public void remove(View view) {
        database.getReference().child("Teachers").child(auth.getUid()).child("class_booked").removeValue();
    }
}
