package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class choice_page extends AppCompatActivity {
    ;
    ConstraintLayout lay;
    public long backPressed;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choice_page);
        checkonline();
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user!=null){
            auth.signOut();
        }
    }

    private void checkonline() {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo=cm.getActiveNetworkInfo();
        if(netinfo!=null&& netinfo.isConnectedOrConnecting())
        {}
        else
        {
            Toast.makeText(choice_page.this,"Please Make Sure You Are Connected",Toast.LENGTH_SHORT).show();
        }
    }

    public void teacher_go(View view) {
        Intent i=new Intent(this,teacherlogin.class);
        startActivity(i);
    }

    public void student_go(View view) {
        Intent i=new Intent(this,studentlogin.class);
        startActivity(i);
    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
