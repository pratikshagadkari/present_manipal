package manipal.com.present_manipal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class splash_screen extends AppCompatActivity {
    int SPLASH_DISPLAY_LENGTH=3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         SharedPreferences preferences=getSharedPreferences("User Info",MODE_PRIVATE);
        final int choice=preferences.getInt("Type", 0);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                    Intent mainIntent = new Intent(splash_screen.this,choice_page.class);
                    startActivity(mainIntent);
                    finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
