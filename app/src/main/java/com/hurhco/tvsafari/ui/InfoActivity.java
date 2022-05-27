package com.hurhco.tvsafari.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hurhco.tvsafari.R;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {
    private ImageView logo;
    private TextView channelName;
    private TextView channelCountry;
    private TextView channelTopic;
    private TextView frequency;
    private TextView website;
    private TextView description;
    private String pic;
    private String name;
    private String country;
    private String topic;
    private String frq;
    private String link;
    private String des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        init();
        getData();
        setData();
    }

    private void init() {
        logo = findViewById(R.id.logo);
        channelName = findViewById(R.id.name_txt);
        channelCountry = findViewById(R.id.country_input_txt);
        channelTopic = findViewById(R.id.topic_input_txt);
        frequency = findViewById(R.id.frequency_input_txt);
        website = findViewById(R.id.website_input_txt);
        description = findViewById(R.id.description_txt);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        pic = bundle.getString("pic");
        name = bundle.getString("name");
        country = bundle.getString("country");
        topic = bundle.getString("topic");
        frq = bundle.getString("frequency");
        link = bundle.getString("link");
        des = bundle.getString("des");
    }
    private void setData() {
        Picasso.get().load(pic).into(logo);
        channelName.setText(name);
        channelCountry.setText(country);
        channelTopic.setText(topic);
        frequency.setText(frq);
        website.setText(link);
        description.setText(des);
    }
}