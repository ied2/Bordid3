package is.aiga.bordid;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private String[] values;
    private LayoutInflater inflater;

    public ListAdapter(Context context, String[] values) {
        this.mContext = context;
        this.values = values;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_restaurant, parent, false);
        }

        // Find views for image and text
        TextView textView = (TextView) convertView.findViewById(R.id.label);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.logo);

        // Text
        textView.setText(values[position]);

        // Image
        if(position == 0) {Picasso.with(this.mContext).load(R.drawable.coco).into(imageView);}
        if(position == 1) {Picasso.with(this.mContext).load(R.drawable.chocho).into(imageView);}
        if(position == 2) {Picasso.with(this.mContext).load(R.drawable.oldspice).into(imageView);}
        if(position == 3) {Picasso.with(this.mContext).load(R.drawable.threeamigos).into(imageView);}
        if(position >= 4) {Picasso.with(this.mContext).load(R.drawable.threeamigos).into(imageView);}

        return convertView;
    }
}