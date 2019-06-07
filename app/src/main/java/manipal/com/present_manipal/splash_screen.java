package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import io.fabric.sdk.android.Fabric;

public class splash_screen extends AppCompatActivity {
    int SPLASH_DISPLAY_LENGTH=2000;

FirebaseAuth auth;
FirebaseUser user;
FirebaseStorage firebaseStorage;
StorageReference storageReference;
FirebaseDatabase data;
DatabaseReference ref;
String imageURL;
final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         SharedPreferences preferences=getSharedPreferences("User Info",MODE_PRIVATE);
        final int choice=preferences.getInt("Type", 0);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                data = FirebaseDatabase.getInstance();
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                ref = data.getReference();
                firebaseStorage=FirebaseStorage.getInstance();
                storageReference=firebaseStorage.getReference();
                if (settings.getBoolean("my_first_time", true)) {
                    Intent i=new Intent(splash_screen.this,app_tutorial.class);
                    startActivity(i);
                    finish();
                    settings.edit().putBoolean("my_first_time", false).apply();
                }
                else{
                    if (user != null) {
                        String s=user.getEmail();
                        Log.d("user_det",auth.getUid());
                        data.getReference().child("Teachers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    storageReference.child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageURL= uri.toString();
                                        }
                                    });
                                    Log.d("user_det",dataSnapshot.child("type").getValue().toString());
                                    if(dataSnapshot.child("type").getValue().toString().equals("teacher")){
                                        Intent mainIntent = new Intent(splash_screen.this, teacher_mainpage.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }

                                }
                                else{
                                    storageReference.child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageURL= uri.toString();
                                        }
                                    });
                                    data.getReference().child("Students").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                if(dataSnapshot.child("type").getValue().toString().equals("student")) {
                                                    Intent mainIntent = new Intent(splash_screen.this, student_mainpage.class);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }
                                            }
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
                    else {
                        Intent mainIntent = new Intent(splash_screen.this, choice_page.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        },SPLASH_DISPLAY_LENGTH);

    }
}
/**/







/**/