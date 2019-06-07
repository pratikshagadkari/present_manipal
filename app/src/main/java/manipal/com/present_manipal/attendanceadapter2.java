package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class attendanceadapter2 extends RecyclerView.Adapter<attendanceadapter2.ViewHolder> {

    public Context mContext;
    ArrayList<attendance_array> list;

    public attendanceadapter2(Context mContext, ArrayList<attendance_array> list) {
        this.mContext = mContext;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.subject_att_with_call,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TextView sub,pre,tot,per;
        sub=holder.sub;
        pre=holder.pre;
        tot=holder.tot;
        per=holder.per;
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no="tel:"+list.get(position).number;
                Intent call=new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse(no));
                mContext.startActivity(call);
            }
        });
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to=list.get(position).email;
                String to1[]=new String[1];
                to1[0]=to;
                Intent em=new Intent(Intent.ACTION_SEND);
                em.putExtra(Intent.EXTRA_EMAIL,to1);
                em.setType("message/rfc822");
                mContext.startActivity(Intent.createChooser(em,"Send Email"));
            }
        });
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
        ImageButton call,email;
        public ViewHolder(View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.sub_name);
            pre = itemView.findViewById(R.id.no_present);
            tot = itemView.findViewById(R.id.total_classes);
            per = itemView.findViewById(R.id.percentage);
            call=itemView.findViewById(R.id.go_call);
            email=itemView.findViewById(R.id.go_email);
        }
    }
}
