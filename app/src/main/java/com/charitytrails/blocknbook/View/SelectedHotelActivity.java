package com.charitytrails.blocknbook.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.charitytrails.blocknbook.Model.Hotel;
import com.charitytrails.blocknbook.Model.User;
import com.charitytrails.blocknbook.R;




public class SelectedHotelActivity extends AppCompatActivity {

    private static final String TAG = "SelectedHotelActivity";
    Button bookButton;
    User user;
    private TextView bookedHotelNameValue, bookedHotelStarsRatingValue, bookedHotelPriceValue;
    private EditText bookingUserLastNameEditText, bookingUserFirstNameEditText, bookingUserEmailEditText, bookingUserNbCompanionsEditText, bookingUserNbNightsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item);

        final Hotel hotel = (Hotel) getIntent().getSerializableExtra("theHotel");
        bookButton = (Button) findViewById(R.id.bookButton);
        user = new User();


        bookedHotelNameValue = (TextView) findViewById(R.id.bookedHotelNameValueTextView);
        bookedHotelStarsRatingValue = (TextView) findViewById(R.id.bookedHotelStarsRatingValueTextView);
        bookedHotelPriceValue = (TextView) findViewById(R.id.bookedHotelPriceValueTextView);



        bookedHotelNameValue.setText(hotel.getName().toString());
        bookedHotelStarsRatingValue.setText(hotel.getStarsRating().toString());
        bookedHotelPriceValue.setText(hotel.getPrice().toString());


        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingUserLastNameEditText = (EditText) findViewById(R.id.bookingUserLastNameEditText);
                bookingUserFirstNameEditText = (EditText) findViewById(R.id.bookingUserFirstNameEditText);
                bookingUserEmailEditText = (EditText) findViewById(R.id.bookingUserEmailEditText);
                bookingUserNbCompanionsEditText = (EditText) findViewById(R.id.bookingUserNbCompanionsEditText);
                bookingUserNbNightsEditText = (EditText) findViewById(R.id.bookingUserNbNightsEditText);

                if (true) {
                    user.setFirstName(bookingUserFirstNameEditText.getText().toString());
                    user.setLastName(bookingUserFirstNameEditText.getText().toString());
                    user.setEmail(bookingUserEmailEditText.getText().toString());



                    Intent bookingIntent = new Intent(SelectedHotelActivity.this, BookHotelActivity.class);

                    startActivity(bookingIntent);

                }


            }
        });

    }
}
