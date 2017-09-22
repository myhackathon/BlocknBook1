package com.charitytrails.blocknbook.View;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charitytrails.blocknbook.Model.Hotel;
import com.charitytrails.blocknbook.R;

import java.util.ArrayList;

public class HotelListAdapter extends ArrayAdapter<Hotel> {


    public HotelListAdapter(Activity context, ArrayList<Hotel> hotels){
              super(context,0, hotels);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_item_layout, parent, false);
        }

        Hotel currentItem = getItem(position);

        System.out.println("Item: "+currentItem);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.nameTextView);
        nameTextView.setText(currentItem.getName());


        TextView starsRatingTextView = (TextView) listItemView.findViewById(R.id.starsRatingTextView);
        starsRatingTextView.setText(getStars(currentItem.getStarsRating()));

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.priceTextView);
        priceTextView.setText(currentItem.getPrice()+"");




        return listItemView;
    }

    private String getStars(int startCount)
    {
        StringBuilder stars = new StringBuilder();
        for(int i =0; i< startCount ; i++)
        {
            stars.append("*");
        }
        return stars.toString();
    }
}
