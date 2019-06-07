package manipal.com.present_manipal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class slideradapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public slideradapter(Context context) {
        this.context = context;
    }

    public int[] sl_images={R.drawable.welcome_onboard,R.drawable.real_time,R.drawable.security,R.drawable.call_email,R.drawable.test1,R.drawable.test2};
    public String[] heading={"Welcome","Real-Time","Secure Attendance","Call & Email","Get Live Updates About College","More Features Soon"};
    public String[] description={"Hello!\nWelcome to PRESENT MANIPAL,our aim is to reduce the the problems with age old attendance systems with our technology",
            "Your attendance is updated Real-Time so you don't have to wait after weeks on end to check your attendance it gets updated after every class",
            "The attendance System is more secure thanks to the various security features we have enabled",
            "Gone are the days when you had to to contact n people to get a person's number,with the way we have designed our database,you can find them in the  app",
            "Inside the student mainpage you can view all the recent events going and about different activities and programs or placement activities happening in our college",
            "We are working on bringing many more features stay tuned!!!"};

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slide_image=view.findViewById(R.id.image_slide);
        TextView slide_text=view.findViewById(R.id.heading);
        TextView desc=view.findViewById(R.id.desc_slide);
        slide_image.setImageResource(sl_images[position]);
        slide_text.setText(heading[position]);
        desc.setText(description[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
