package manipal.com.present_manipal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class attendanceadapter extends RecyclerView.Adapter<attendanceadapter.ViewHolder> {

    public Context mContext;
    ArrayList<attendance_array> list;

    public attendanceadapter(Context mContext, ArrayList<attendance_array> list) {
        this.mContext = mContext;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.subject_attendance_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView sub,pre,tot,per;
        sub=holder.sub;
        pre=holder.pre;
        tot=holder.tot;
        per=holder.per;
        Log.d("lolol1",list.get(position).sub);
        sub.setText(list.get(position).sub);
        pre.setText("No of Present="+String.valueOf( list.get(position).pre));
        per.setText(String.valueOf(list.get(position).per));
        tot.setText("Total Classes="+String.valueOf(list.get(position).tot));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tot,pre,per,sub;

        public ViewHolder(View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.sub_name);
            pre = itemView.findViewById(R.id.no_present);
            tot = itemView.findViewById(R.id.total_classes);
            per = itemView.findViewById(R.id.percentage);
        }
    }
}
