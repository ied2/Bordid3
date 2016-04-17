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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// This class creates every single item in our recycleView list
public class RAdapter extends RecyclerView.Adapter<RAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Reservation> data = Collections.emptyList(); // Collections.emptyList() prevents null exception
    private Context context;

    public RAdapter(Context context, List<Reservation> data) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_reservation, parent, false);
//        view.setOnClickListener(new MyOnClickListener());
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Reservation current = data.get(position); // get current item from our list at given position


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //get current date time with Date()
        Date currentDate = new Date();

        holder.name.setText(current.getRestaurant().getName());
        holder.numseats.setText(String.valueOf(current.getNumSeats()));
        holder.address.setText(current.getRestaurant().getAddress());
        String date = current.getReservationDate().substring(0, current.getReservationDate().length() - 3);
        holder.date.setText(current.getReservationDate().substring(0, current.getReservationDate().length() - 3));

        try {
            Date date2 = dateFormat.parse(date);
            Log.d("IED", currentDate.toString());
            Log.d("IED", date2.toString());
            Log.d("IED", "bool: " +date2.after(currentDate));
            if(!date2.after(currentDate)) {
                holder.name.setAlpha(.5f);
                holder.numseats.setAlpha(.5f);
                holder.address.setAlpha(.5f);
                holder.date.setAlpha(.5f);
                holder.count.setAlpha(.5f);
            }
            else {
                holder.name.setAlpha(1f);
                holder.numseats.setAlpha(1f);
                holder.address.setAlpha(1f);
                holder.date.setAlpha(1f);
                holder.count.setAlpha(1f);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView numseats, name, address, date, count;
        ImageView logo;
        View reservation_item;

        public MyViewHolder(View itemView) {
            super(itemView);

            count = (TextView) itemView.findViewById(R.id.count);
            reservation_item = itemView.findViewById(R.id.reservation_item);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            numseats = (TextView) itemView.findViewById(R.id.numseats);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = NearMeActivity.recyclerView.getChildAdapterPosition(v);

//            Intent i = new Intent(context, RestaurantInfoActivity.class);
//            i.putExtra("id", data.get(position).getId());
//            i.putExtra("name", data.get(position).getName());
//            i.putExtra("phoneNumber", data.get(position).getPhoneNumber());
//            i.putExtra("url", data.get(position).getWebsite());
//            i.putExtra("description", data.get(position).getDescription());
//            i.putExtra("address", data.get(position).getAddress());
//            i.putExtra("logo", data.get(position).getLogo());
//            context.startActivity(i);
        }
    }
}
