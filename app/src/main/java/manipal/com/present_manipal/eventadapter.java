package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class eventadapter extends RecyclerView.Adapter<eventadapter.ViewHolder> {
    public Context mContext;
    ArrayList<disp_event> list;
    ImageView red_heart,white_heart;
    GestureDetector gestureDetector;
    heart_toggle heart;

    public eventadapter(Context mContext, ArrayList<disp_event> list) {
        this.mContext = mContext;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.events_layout,parent,false);
        eventadapter.ViewHolder viewHolder=new eventadapter.ViewHolder(view);
        //red_heart=view.findViewById(R.id.like2);
        //white_heart=view.findViewById(R.id.like);
        gestureDetector=new GestureDetector(mContext,new GestureListener());
        //heart=new heart_toggle(red_heart,white_heart);
        //toggleLike();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        ImageView a;
        Button b;
        TextView c;
        a=holder.a;
        b=holder.b;
        c=holder.name;
        c.setText(list.get(position).b);
        Glide.with(mContext).load(list.get(position).c).apply(new RequestOptions().centerCrop()).into(a);
        b.setText("Visit");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(list.get(position).d));
                mContext.startActivity(intent);

            }
        });
    }
    public void toggleLike(){
        red_heart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        white_heart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            heart.toggle();
            return true;
        }
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView a,c;
        ImageView red,white;
        TextView name;
        Button b;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.event_name);
            a=itemView.findViewById(R.id.event_image);
            b=itemView.findViewById(R.id.go_page);
            //c=itemView.findViewById(R.id.like);
        }
    }
}
