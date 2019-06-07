package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class choice_page extends AppCompatActivity {
    Snackbar sb;
    ConstraintLayout lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choice_page);
        checkonline();
        //Toast.makeText(choice_page.this,"Data/Wifi should be on",Toast.LENGTH_SHORT).show();

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
    @Override
    public void onBackPressed()
    {
        //sb=Snackbar.make(lay,"Press Back Again To Exit",Snackbar.LENGTH_SHORT);
        if(sb.isShown())
            super.onBackPressed();
        else
            sb.show();
    }

}
