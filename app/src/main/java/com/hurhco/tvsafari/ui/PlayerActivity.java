package com.hurhco.tvsafari.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.hurhco.tvsafari.R;

import java.util.HashMap;
import java.util.Map;

public class PlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    private String link;
    private String referer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");
        referer = bundle.getString("referer");

        simpleExoPlayer = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(new DefaultMediaSourceFactory(headers())).build();
        playerView = findViewById(R.id.exo_player_view);
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(link);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
    }

    public DataSource.Factory headers() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Referer", referer);
        return new DefaultHttpDataSource.Factory().setDefaultRequestProperties(headersMap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.stop();
        simpleExoPlayer.seekTo(0);
    }
}