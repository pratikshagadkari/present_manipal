package manipal.com.present_manipal;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class teacher_mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth;
    FirebaseDatabase data;
    FirebaseUser user;
    ArrayList<disp_event> list;
    eventadapter adapter;
    Uri image;
    RecyclerView recyclerView;
    ImageView img;
    String imageURL;
    FirebaseStorage storage;
    StorageReference storageRef;
    TextView name,email;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_mainpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth=FirebaseAuth.getInstance();
        data= FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();
        ref=data.getReference();
        storage=FirebaseStorage.getInstance();
        storageRef=storage.getReference().child(auth.getUid());
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /*if(!user.isEmailVerified()){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(teacher_mainpage.this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(teacher_mainpage.this);
            }
            builder.setCancelable(false);
            builder.setTitle("Email Verification")
                    .setMessage("Email Not Verified")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView=navigationView.getHeaderView(0);
        img=navigationView.getHeaderView(0).findViewById(R.id.head_image);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageURL= uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        email=navigationView.getHeaderView(0).findViewById(R.id.email_view);
        email.setText(user.getEmail());
        name=navigationView.getHeaderView(0).findViewById(R.id.name_view);
        ref.child("Teachers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("abcd",dataSnapshot.getValue().toString());
                user_details abc=dataSnapshot.getValue(user_details.class);
                name.setText(abc.uname);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.child("Teachers").child(auth.getUid()).child("add_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue().toString().compareTo("empty")==0){
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(teacher_mainpage.this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(teacher_mainpage.this);
                        }
                        builder.setCancelable(false);
                        builder.setTitle("Update Info")
                                .setMessage("We Need More Info So We Can Search You Up Faster In Our Database!!")
                                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(teacher_mainpage.this,update_info_teachers.class);
                                        startActivity(i);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //adapter.setHasStableIds(true);
        list=new ArrayList<disp_event>();
        data.getReference().child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String event=dataSnapshot1.getKey();
                    String url=dataSnapshot1.child("Image").getValue().toString();
                    String site=dataSnapshot1.child("Website").getValue().toString();
                    Log.d("abcdef",event);
                    Log.d("abcdef",url);
                    Log.d("abcdef",site);
                    list.add(new disp_event(R.drawable.about_srijit,event,url,site));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(teacher_mainpage.this);
        RecyclerView.LayoutManager layoutManager1=layoutManager;
        recyclerView.setLayoutManager(layoutManager1);
        adapter=new eventadapter(teacher_mainpage.this,list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this,"Back Press Has Been Disabled",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_mainpage, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.about){
            Intent i= new Intent(this,about_us.class);
            startActivity(i);
        }
        if (id == R.id.update) {
            Intent i=new Intent(teacher_mainpage.this,update_info_teachers.class);
            startActivity(i);

        } else if (id == R.id.view_attendance) {
            Intent i=new Intent(teacher_mainpage.this,view_attendance_teacher.class);
            startActivity(i);

        } else if (id == R.id.free_class) {
            Intent i = new Intent(teacher_mainpage.this,disp_free_class.class);
            startActivity(i);

        } else if (id == R.id.contact) {
            Intent i=new Intent(teacher_mainpage.this,contact_teachers.class);
            startActivity(i);
        } else if (id == R.id.attendance) {
            Intent i=new Intent(teacher_mainpage.this,manage_attendance.class);
            startActivity(i);
        } else if (id == R.id.support) {
            Intent i= new Intent(this,support.class);
            startActivity(i);

        }else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(this,choice_page.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private static final int request=1;
    public void acd(View view) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    10);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,request);
    }
    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data1){
        super.onActivityResult(requestcode,resultcode,data1);
        if(resultcode==RESULT_OK) {
            image = data1.getData();
            storageRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    img.setImageURI(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(teacher_mainpage.this,"Error Please Try Again",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
