package com.charitytrails.blocknbook.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.charitytrails.blocknbook.Model.Hotel;
import com.charitytrails.blocknbook.R;

import java.util.ArrayList;


public class HotelListActivity extends AppCompatActivity {

    private static final String TAG = "HotelListActivity";


    String location;

    SimpleCursorAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        setLocation(bundle.getString("location"));
        mListView = (ListView) findViewById(R.id.hotelsListview);

        ArrayList<Hotel> hotelArrayList = new ArrayList<Hotel>();


        hotelArrayList.add(new Hotel("J W Mariot Bangalore", new Integer(5),new Double(75)));
        hotelArrayList.add(new Hotel("Leela Palace", new Integer(5),new Double(100)));
        hotelArrayList.add(new Hotel("Orchid Hotel", new Integer(5) ,new Double(80)));

        HotelListAdapter hotelListAdapter = new HotelListAdapter(this, hotelArrayList);
        mListView.setAdapter(hotelListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
                TextView starsRatingTextView = (TextView) view.findViewById(R.id.starsRatingTextView);
                TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);

                Hotel hotel = new Hotel(nameTextView.getText().toString(), getStars(starsRatingTextView.getText().toString()), Double.parseDouble(priceTextView.getText().toString()));

                Intent bookingIntent = new Intent(HotelListActivity.this,SelectedHotelActivity.class);
                bookingIntent.putExtra("theHotel",hotel);
                startActivity(bookingIntent);
                Log.i(TAG, hotel.toString());

            }
        });


    }


    private Integer getStars(String starText)
    {
        return starText != null ? starText.trim().length() :0;

    }


    public void setLocation(String location) {
        this.location = location;
    }


}
