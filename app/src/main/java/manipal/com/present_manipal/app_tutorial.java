package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class app_tutorial extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private  slideradapter slideradapter1;
    private TextView[] dots;
    int pagepos;

    Button backbtn,nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tutorial);
        final SharedPreferences settings=getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        settings.edit().putBoolean("my_first_time", false).apply();
        backbtn=findViewById(R.id.prev);
        nextbtn=findViewById(R.id.next);
        viewPager=findViewById(R.id.viewPager);
        linearLayout=findViewById(R.id.dots);
        slideradapter1=new slideradapter(this);
        viewPager.setAdapter(slideradapter1);
        viewPager.setOffscreenPageLimit(6);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(pagepos+1);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(pagepos-1);
            }
        });
    }
    public void addDotsIndicator(int position){
        dots=new TextView[6];
        linearLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            linearLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(Color.parseColor("#ffffff"));
        }
    }
    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            pagepos=position;
            if(position==0){
                nextbtn.setEnabled(true);
                backbtn.setEnabled(false);
                backbtn.setVisibility(View.INVISIBLE);
                backbtn.setText("");
                nextbtn.setText("Next");
            }else if(position== dots.length-1){
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
                backbtn.setVisibility(View.VISIBLE);
                backbtn.setText("Back");
                nextbtn.setText("Finish");
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(nextbtn.getText().equals("Finish")) {
                            Intent i = new Intent(app_tutorial.this, choice_page.class);
                            SharedPreferences s = getApplicationContext().getSharedPreferences("my_first_time", Context.MODE_PRIVATE);
                            s.edit().putBoolean("my_first_time", false).apply();
                            startActivity(i);
                            finish();
                        }else {
                            viewPager.setCurrentItem(pagepos+1);
                        }
                    }
                });
            }else{
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
                backbtn.setVisibility(View.VISIBLE);
                backbtn.setText("Back");
                nextbtn.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
