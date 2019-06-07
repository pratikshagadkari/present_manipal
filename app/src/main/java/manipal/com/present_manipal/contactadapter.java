package manipal.com.present_manipal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class contactadapter extends RecyclerView.Adapter<contactadapter.ViewHolder> implements Filterable {
    public Context mContext;
    ArrayList<contacts_array> list;
    ArrayList<contacts_array> list2;
    public contactadapter(Context mContext, ArrayList<contacts_array> list) {
        this.mContext = mContext;
        this.list=list;
        list2=new ArrayList<>(list);
    }

    @NonNull
    @Override
    public contactadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.contact_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull contactadapter.ViewHolder holder, int position) {
        TextView na,ph,em;
        ImageButton call,email;
        call=holder.but;
        email=holder.but1;
        na=holder.name;
        ph=holder.phone;
        em=holder.email;
        na.setText(list.get(position).name);
        ph.setText(list.get(position).phone);
        em.setText(list.get(position).email);
        final String number="tel:"+list.get(position).phone;
        final String email_id[]={list.get(position).email};
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(number));
                mContext.startActivity(i);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent em=new Intent(Intent.ACTION_SEND);
                em.putExtra(Intent.EXTRA_EMAIL,email_id);
                em.setType("message/rfc822");
                mContext.startActivity(Intent.createChooser(em,"Send Email"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public Filter getFilter() {
        return listFilter;
    }
    private Filter listFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<contacts_array>filtered=new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filtered.addAll(list2);
            }
            else {
                String pattern = constraint.toString().toLowerCase().trim();
                for(contacts_array item:list2){
                    if(item.name.toLowerCase().contains(pattern)){
                        filtered.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,phone,email;
        ImageButton but,but1;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
            email=itemView.findViewById(R.id.email);
            but=itemView.findViewById(R.id.go_call);
            but1=itemView.findViewById(R.id.go_email);
        }
    }
}
