package com.livetv.app;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        String url = getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        ((android.widget.TextView) findViewById(R.id.playerTitle)).setText(title == null ? "Live TV" : title);
        PlayerView playerView = findViewById(R.id.playerView);
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        if (url != null && !url.isEmpty()) {
            player.setMediaItem(MediaItem.fromUri(url));
            player.prepare();
            player.play();
        }
    }

    @Override protected void onStop() {
        super.onStop();
        if (player != null) player.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
