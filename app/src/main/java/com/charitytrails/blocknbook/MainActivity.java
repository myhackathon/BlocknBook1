package com.charitytrails.blocknbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.charitytrails.blocknbook.Controller.HotelManager;
import com.charitytrails.blocknbook.View.HotelListActivity;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    Context context;
    HotelManager hotelManager ;

    Button searchHotel;
    TextView hotelToSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();


        hotelToSearch = (TextView) findViewById(R.id.edit_origin);



        searchHotel = (Button) findViewById(R.id.searchHotelButton);
        searchHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                Intent hotelsListIntent = new Intent(MainActivity.this,HotelListActivity.class);
                String hotelLocation = hotelToSearch.getText().toString();


                if (hotelLocation != null || hotelLocation != ""){
                    bundle.putString("location",hotelLocation);
                    hotelsListIntent.putExtras(bundle);
                    startActivity(hotelsListIntent);
                }

            }
        });

    }


}