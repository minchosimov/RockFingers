package com.mincho.rockfingers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //add action bar
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView verTV = (TextView) findViewById(R.id.about_vesion);
        String verTxt = getResources().getString(R.string.ver).concat(" ").concat(BuildConfig.VERSION_NAME);
        verTV.setText(verTxt);

        Button rateButton = (Button) findViewById(R.id.button_rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + getPackageName())));
                }
            }
        });

    }
}
