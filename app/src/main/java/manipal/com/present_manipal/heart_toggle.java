package manipal.com.present_manipal;

import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;

public class heart_toggle {
    ImageView redheart,whiteheart;

    public heart_toggle(ImageView redheart, ImageView whiteheart) {
        this.redheart = redheart;
        this.whiteheart = whiteheart;
    }
    public void toggle(){
        AnimatorSet animatorSet=new AnimatorSet();
        if(redheart.getVisibility()==View.VISIBLE){
            redheart.setVisibility(View.INVISIBLE);
        }
        else if(redheart.getVisibility()==View.INVISIBLE){
            redheart.setVisibility(View.VISIBLE);
        }
    }
}
