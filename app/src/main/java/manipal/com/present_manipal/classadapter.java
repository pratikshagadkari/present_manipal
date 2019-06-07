package manipal.com.present_manipal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class classadapter extends RecyclerView.Adapter<classadapter.ViewHolder> {
    public Context mContext;
    ArrayList<class_array> list;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public classadapter(Context mContext, ArrayList<class_array> list) {

        this.mContext = mContext;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.class_layout,parent,false);
        classadapter.ViewHolder viewHolder=new classadapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.name.setText(list.get(position).room);
        final String time=list.get(position).time;
        final String dat=list.get(position).day;
        final String room=list.get(position).room;
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Class").child(dat).child(time).child(room).setValue("filled");
                database.getReference().child("Teachers").child(auth.getUid()).child("class_booked").setValue(room);
                Toast.makeText(mContext,"Booked Your Slot",Toast.LENGTH_SHORT).show();
            }
        });
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

        ImageView red,white;
        TextView name;
        Button b;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.event_name);
            b=itemView.findViewById(R.id.book);

        }
    }

}
