package com.example.shuffle;

import static com.example.shuffle.MainActivity.mySongs;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    Button btnPlay,btnNext,btnPrevious,btnFastForward,btnFastBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar seekMusicBar;

    ImageView imageView;

    String songName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position = 0;
    static ArrayList<File> listSongs = new ArrayList<>();
    static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_pa_layout);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();

    }
    private void getIntentMethod()
    {
        position = getIntent().getIntExtra("pos",0);
        listSongs = mySongs;
        txtSongName.setSelected(true);
        songName = listSongs.get(position).getName().replace(".mp3","").replace(".wav","");
        txtSongName.setText(songName);
        if(listSongs != null)
        {
            uri = Uri.parse(listSongs.get(position).getPath());
        }

        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        try {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews()
    {
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);
        btnFastForward = findViewById(R.id.btnFastForward);
        btnFastBackward = findViewById(R.id.btnFastBackward);

        txtSongName = findViewById(R.id.txtView);
        txtSongStart = findViewById(R.id.txtViewStart);
        txtSongEnd = findViewById(R.id.txtViewEnd);

        seekMusicBar = findViewById(R.id.seekBar);


        imageView = findViewById(R.id.imgView);
    }
}