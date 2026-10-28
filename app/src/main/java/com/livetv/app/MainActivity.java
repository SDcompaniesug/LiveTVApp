package com.livetv.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final List<Channel> allChannels = new ArrayList<>();
    private final List<String> categories = Arrays.asList("All", "News", "Sports", "Entertainment", "General");
    private ChannelAdapter adapter;
    private LinearLayout filterContainer;
    private TextView emptyView;
    private String selectedCategory = "All";
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadChannels();

        RecyclerView list = findViewById(R.id.channelsRecycler);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChannelAdapter(allChannels, channel -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra(PlayerActivity.EXTRA_URL, channel.url);
            intent.putExtra(PlayerActivity.EXTRA_TITLE, channel.name);
            startActivity(intent);
        });
        list.setAdapter(adapter);

        filterContainer = findViewById(R.id.channelFilters);
        emptyView = findViewById(R.id.channelsEmpty);
        findViewById(R.id.channelsLoading).setVisibility(View.GONE);
        buildFilters();
        findViewById(R.id.liveSearchButton).setOnClickListener(v -> showSearchDialog());
        applyFilters();
    }

    private void loadChannels() {
        // Demo streams only. Replace with channels you are authorized to distribute.
        String demo = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8";
        allChannels.add(new Channel("Demo News", "News", demo));
        allChannels.add(new Channel("Demo Sports", "Sports", demo));
        allChannels.add(new Channel("Demo Entertainment", "Entertainment", demo));
    }

    private void buildFilters() {
        filterContainer.removeAllViews();
        for (String category : categories) {
            TextView chip = new TextView(this);
            chip.setText(category);
            chip.setTextSize(13);
            chip.setTypeface(null, category.equals(selectedCategory) ? Typeface.BOLD : Typeface.NORMAL);
            chip.setTextColor(category.equals(selectedCategory) ? Color.WHITE : Color.rgb(169, 177, 182));
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(dp(15), dp(9), dp(15), dp(9));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, dp(36));
            lp.setMarginEnd(dp(8));
            chip.setLayoutParams(lp);
            chip.setBackgroundResource(category.equals(selectedCategory)
                    ? R.drawable.bg_selected : R.drawable.bg_channel_item);
            chip.setOnClickListener(v -> {
                selectedCategory = category;
                buildFilters();
                applyFilters();
            });
            filterContainer.addView(chip);
        }
    }

    private void showSearchDialog() {
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("Search channels");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(searchQuery);
        new AlertDialog.Builder(this)
                .setTitle("Search channels")
                .setView(input)
                .setPositiveButton("Search", (dialog, which) -> {
                    searchQuery = input.getText().toString().trim();
                    applyFilters();
                })
                .setNeutralButton("Clear", (dialog, which) -> {
                    searchQuery = "";
                    applyFilters();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void applyFilters() {
        if (adapter == null) return;
        List<Channel> filtered = new ArrayList<>();
        for (Channel channel : allChannels) {
            boolean categoryMatches = selectedCategory.equals("All")
                    || channel.category.equalsIgnoreCase(selectedCategory);
            boolean queryMatches = searchQuery.isEmpty()
                    || channel.name.toLowerCase(Locale.ROOT).contains(searchQuery.toLowerCase(Locale.ROOT))
                    || channel.category.toLowerCase(Locale.ROOT).contains(searchQuery.toLowerCase(Locale.ROOT));
            if (categoryMatches && queryMatches) filtered.add(channel);
        }
        adapter.setChannels(filtered);
        emptyView.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        emptyView.setText(searchQuery.isEmpty() ? "No channels available" : "No channels match your search");
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}