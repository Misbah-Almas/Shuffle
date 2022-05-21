package com.example.shuffle;

import static com.example.shuffle.MainActivity.mySongs;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    Button btnPlay,btnNext,btnPrevious,btnFastForward,btnFastBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar seekMusicBar;

    BarVisualizer barVisualizer;

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

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                {
                    btnPlay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else
                {
                    btnPlay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();

                    TranslateAnimation moveAnim = new TranslateAnimation(-25,25,-25,25);
                    moveAnim.setInterpolator(new AccelerateInterpolator());
                    moveAnim.setDuration(600);
                    moveAnim.setFillEnabled(true);
                    moveAnim.setFillAfter(true);
                    moveAnim.setRepeatMode(Animation.REVERSE);
                    moveAnim.setRepeatCount(1);
                    imageView.startAnimation(moveAnim);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%listSongs.size());
                Uri uri = Uri.parse(listSongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                songName = listSongs.get(position).getName().replace(".mp3","").replace(".wav","");
                txtSongName.setText(songName);
                try {
                    mediaPlayer.start();
                }
                catch (Exception e)
                {
                   e.printStackTrace();
                }

                startAnimation(imageView,360f);
            }
        });

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
        barVisualizer = findViewById(R.id.wave);
        imageView = findViewById(R.id.imgView);
    }

    public void startAnimation(View view,Float degree)
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",0f,degree);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }
}