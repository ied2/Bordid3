package is.aiga.bordid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Random;

// This class creates every single item in our recycleView list
public class VAdapter extends RecyclerView.Adapter<VAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Restaurant> data = Collections.emptyList(); // Collections.emptyList() prevents null exception
    private Context context;

    public VAdapter(Context context, List<Restaurant> data) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.data = data;
    }


    public void update(List<Restaurant> data) {
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_restaurant, parent, false);
        view.setOnClickListener(new MyOnClickListener());
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Restaurant current = data.get(position); // get current item from our list at given position

        holder.name.setText(current.getName());

        holder.address.setText(current.getAddress());

        holder.price.setText("Average Price " + current.getPrice() + " kr");

        // Check if the restaurant is open or closed
        if(current.isOpen()) {
            holder.open.setTextColor(Color.parseColor("#00DE0E")); // Green
            holder.open.setText("Open");
        }else {
            holder.open.setTextColor(Color.parseColor("#D32F2F")); // Red
            holder.open.setText("Closed");
        }

        // Display image of restaurant
        if(current.getLogo().equals("999")) Picasso.with(this.context).load(R.drawable.upload_image).fit().into(holder.logo);
        else Picasso.with(this.context).load(current.getLogo()).fit().into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView name, open, address, price;
        ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);

            logo = (ImageView) itemView.findViewById(R.id.logo);
            name = (TextView) itemView.findViewById(R.id.label);
            open = (TextView) itemView.findViewById(R.id.opening);
            address = (TextView) itemView.findViewById(R.id.RestAddress);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = NearMeActivity.recyclerView.getChildAdapterPosition(v);

            Intent i = new Intent(context, RestaurantInfoActivity.class);
            i.putExtra("id", data.get(position).getId());
            i.putExtra("name", data.get(position).getName());
            i.putExtra("phoneNumber", data.get(position).getPhoneNumber());
            i.putExtra("url", data.get(position).getWebsite());
            i.putExtra("description", data.get(position).getDescription());
            i.putExtra("address", data.get(position).getAddress());
            i.putExtra("logo", data.get(position).getLogo());
            context.startActivity(i);
        }
    }
}
