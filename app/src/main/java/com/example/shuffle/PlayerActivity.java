package com.example.shuffle;

import static com.example.shuffle.MainActivity.mySongs;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    static MediaPlayer mediaPlayer;
    int position = -1;
    static ArrayList<File> listSongs = new ArrayList<>();
    static Uri uri;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            updateSeekBar.interrupt();
        }
        if (barVisualizer != null)
        {
            barVisualizer.release();
        }
        super.onDestroy();
    }

    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_pa_layout);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
        getIntentMethod();

    }
    private void getIntentMethod()
    {
        position = getIntent().getIntExtra("pos",-1);
        listSongs = mySongs;
        txtSongName.setSelected(true);
        songName = listSongs.get(position).getName().replace(".mp3","").replace(".wav","");
        txtSongName.setText(songName);
        if(listSongs != null)
        {
           uri = Uri.parse(listSongs.get(position).toString());
        }

        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        updateSeekBar = new Thread()
        {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition<totalDuration)
                {
                    try{
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekMusicBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException | IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekMusicBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekMusicBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_500), PorterDuff.Mode.MULTIPLY);
        seekMusicBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_500),PorterDuff.Mode.SRC_IN);

        seekMusicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtSongStart.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);

        btnPlay.setOnClickListener(view -> {
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
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> btnNext.performClick());

        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1)
        {
            barVisualizer.setAudioSessionId(audioSessionId);
        }

        btnNext.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position+1)%listSongs.size());
            Uri uri = Uri.parse(listSongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            songName = listSongs.get(position).getName().replace(".mp3","").replace(".wav","");
            txtSongName.setText(songName);
            seekMusicBar.setMax(mediaPlayer.getDuration());
            txtSongEnd.setText(createTime(mediaPlayer.getDuration()));
            btnPlay.setBackgroundResource(R.drawable.ic_pause);
            int audioSessionId1 = mediaPlayer.getAudioSessionId();
            if (audioSessionId1 != -1)
            {
                barVisualizer.setAudioSessionId(audioSessionId1);
            }

            mediaPlayer.start();

            startAnimation(360f);
        });

        btnPrevious.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position-1)<0)?(mySongs.size()-1):position-1;
            Uri uri = Uri.parse(listSongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            songName = listSongs.get(position).getName().replace(".mp3","").replace(".wav","");
            txtSongName.setText(songName);
            seekMusicBar.setMax(mediaPlayer.getDuration());
            txtSongEnd.setText(createTime(mediaPlayer.getDuration()));
            btnPlay.setBackgroundResource(R.drawable.ic_pause);
            int audioSessionId12 = mediaPlayer.getAudioSessionId();
            if (audioSessionId12 != -1)
            {
                barVisualizer.setAudioSessionId(audioSessionId12);
            }

            mediaPlayer.start();

            startAnimation(-360f);
        });

        btnFastForward.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
            }
        });

        btnFastBackward.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
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

    public void startAnimation(Float degree)
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",0f,degree);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    public String createTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time = time+min+":";
        if (sec<10)
        {
            time+="0";
        }
        time+=sec;
        return time;
    }
}