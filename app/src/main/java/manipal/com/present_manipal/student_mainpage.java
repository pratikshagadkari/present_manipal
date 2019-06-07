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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class student_mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "rest";
    FirebaseAuth auth;
    FirebaseDatabase data;
    FirebaseUser user;
    ImageView img;
    TextView name,email;
    String text;
    RecyclerView recyclerView;
    String imageURL;
    ArrayList<disp_event> list;
    String r;
    FirebaseDatabase secondaryDatabase;
    Uri image,image_dest;
    eventadapter adapter;
    FirebaseStorage storage;
    StorageReference storageRef,image_path ;
    DatabaseReference ref,ref1;
    ProgressBar progressBar;
    FirebaseApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        data= FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();
        ref=data.getReference();
        storage=FirebaseStorage.getInstance();
        storageRef=storage.getReference().child(auth.getUid());
        setContentView(R.layout.activity_student_mainpage);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ref.child("Students").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_details abc=dataSnapshot.getValue(user_details.class);
                name.setText(abc.uname);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*if(!user.isEmailVerified()){
                     AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(student_mainpage.this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(student_mainpage.this);
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
        ref.child("Students").child(auth.getUid()).child("add_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue().toString().compareTo("empty")==0){
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(student_mainpage.this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(student_mainpage.this);
                        }
                        builder.setCancelable(false);
                        builder.setTitle("Update Info")
                                .setMessage("We Need More Info So We Can Search You Up Faster In Our Database!!")
                                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(student_mainpage.this,update_info_students.class);
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

        list=new ArrayList<>();
        //adapter.setHasStableIds(true);
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
        LinearLayoutManager layoutManager=new LinearLayoutManager(student_mainpage.this);
        RecyclerView.LayoutManager layoutManager1=layoutManager;
        recyclerView.setLayoutManager(layoutManager1);
        adapter=new eventadapter(student_mainpage.this,list);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);

        onTokenRefresh();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Toast.makeText(this,"Back Press Has Been Disabled",Toast.LENGTH_SHORT).show();
        }
    }

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "asd " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       // sendRegistrationToServer(refreshedToken);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_mainpage, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.attendance) {

            data.getReference().child("Students").child(auth.getUid()).child("regd_no").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String abc=dataSnapshot.getValue().toString();
                        Intent i=new Intent(student_mainpage.this,view_att_stud.class);
                        i.putExtra("regd_no",abc);
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(id==R.id.about){
            Intent i = new Intent(this, about_us.class);
            startActivity(i);
        }else if (id == R.id.update) {
            Intent i=new Intent(student_mainpage.this,update_info_students.class);
            startActivity(i);

        }else if (id == R.id.notification) {
            Intent i=new Intent(student_mainpage.this,string_receive.class);
            startActivity(i);
        } else if (id == R.id.contact) {
            Intent i=new Intent(student_mainpage.this,contact_students.class);
            startActivity(i);

        } else if (id == R.id.support) {
            Intent i= new Intent(this,support.class);
            startActivity(i);

        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(this,choice_page.class);
            startActivity(i);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private static final int request=1;

    public void notify_user(View view) {
    }
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
            image_dest=Uri.fromFile(new File(getCacheDir(),"Cropped"));
            storageRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    img.setImageURI(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(student_mainpage.this,"Error Please Try Again",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
