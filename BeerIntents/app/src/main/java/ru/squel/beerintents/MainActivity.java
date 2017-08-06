package ru.squel.beerintents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Button handler. Send text to another activity of this app
     * @param view
     */
    public void onClickActivity(View view) {
        Intent intent = new Intent(this, TextActivity.class);
        intent.putExtra(TextActivity.EXTRA_MESSAGE, formBeerDescription());
        startActivity(intent);
    }

    /**
     * Button handler. Send text to another activity of another app,
     * selected by user
     * @param view
     */
    public void onClickSendIntent(View view) {
        // form first intent with data to system
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, formBeerDescription());

        // form second intent to another app from system
        Intent chosenIntent = Intent.createChooser(intent, "Send by ...");
        startActivity(chosenIntent);
    }

    /**
     * Form description of sort of the beer
     * @return
     */
    private String formBeerDescription() {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        int selectedBeer = spinner.getSelectedItemPosition();
        String answer;
        String[] beerColors = getResources().getStringArray(R.array.beer_descriptions);
        answer = beerColors[selectedBeer];
        return  answer;
    }

}
