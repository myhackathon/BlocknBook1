package com.charitytrails.blocknbook.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.charitytrails.blocknbook.MainActivity;
import com.charitytrails.blocknbook.R;


public class BookHotelActivity extends AppCompatActivity {

    private static final String TAG = "BookHotelActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item);

        Button bookButton = (Button) findViewById(R.id.bookButton1);
        bookButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent bookingIntent = new Intent(BookHotelActivity.this, FullWalletConfirmationButtonActivity.class);
                        startActivity(bookingIntent);
                    }
                });


    }
}
