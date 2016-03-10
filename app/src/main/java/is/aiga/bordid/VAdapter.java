package is.aiga.bordid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

public class VAdapter extends RecyclerView.Adapter<VAdapter.MyViewHolder> implements View.OnClickListener {

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList(); // Collections.emptyList() prevents null exception
    private Context context;

    public VAdapter(Context context, List<Information> data) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_restaurant, parent, false);
        view.setOnClickListener(this);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Information current = data.get(position); // get current item from our list at given position

        holder.title.setText(current.rName);

        if(current.rImage.equals("999")) Picasso.with(this.context).load(R.drawable.upload_image).fit().into(holder.logo);
        else Picasso.with(this.context).load(current.rImage).fit().into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);

            logo = (ImageView) itemView.findViewById(R.id.logo);
            title = (TextView) itemView.findViewById(R.id.label);
        }
    }

    @Override
    public void onClick(View v) {
//        Intent i = new Intent(context, )
    }
}
