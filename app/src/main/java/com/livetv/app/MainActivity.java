package com.livetv.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Channel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadChannels();
        RecyclerView list = findViewById(R.id.channelList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new ChannelAdapter(channels, channel -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra(PlayerActivity.EXTRA_URL, channel.url);
            intent.putExtra(PlayerActivity.EXTRA_TITLE, channel.name);
            startActivity(intent);
        }));
    }

    private void loadChannels() {
        // Replace these sample URLs with streams you are licensed to distribute.
        String demo = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8";
        channels.add(new Channel("Demo News", "NEWS", demo));
        channels.add(new Channel("Demo Sports", "SPORTS", demo));
        channels.add(new Channel("Demo Entertainment", "ENTERTAINMENT", demo));
    }
}
