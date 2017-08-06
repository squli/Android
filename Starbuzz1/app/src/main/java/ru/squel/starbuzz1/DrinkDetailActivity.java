package ru.squel.starbuzz1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DrinkDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKNO = "EXTRA_DRINKNO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        int index = (int)getIntent().getExtras().get(EXTRA_DRINKNO);

        ImageView photo = (ImageView) findViewById(R.id.photo);

        TextView stringTop = (TextView) findViewById((R.id.name));
        TextView stringBot = (TextView) findViewById((R.id.description));

        Drink drink = Drink.drinksAll.get(index);

        photo.setImageResource(drink.getImageResourceId());
        photo.setContentDescription(drink.getName());
        stringTop.setText(drink.getName());
        stringBot.setText(drink.getDescription());
    }

}
