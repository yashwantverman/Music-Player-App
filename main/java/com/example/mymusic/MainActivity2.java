package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.security.cert.Extension;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    TextView textView;
    Thread seekbarUpdate;
    SeekBar seekBar;
    ImageView pause,next,previous,imageView;
    ArrayList<File> songs;
    String songNme;
    MediaPlayer mediaPlayer;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView=findViewById(R.id.imageView);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        pause=findViewById(R.id.pause);
        seekBar=findViewById(R.id.seekBar);
        textView=findViewById(R.id.textView);
        Intent intent=getIntent();
        Bundle bundle =intent.getExtras();
        songs  =(ArrayList) bundle.getParcelableArrayList("songList");
        songNme=intent.getStringExtra("currentSong");
        textView.setText(songNme);
        textView.setSelected(true);
         position =intent.getIntExtra("position",0);
         Uri uri=Uri.parse((songs.get(position).toString()));
         mediaPlayer=mediaPlayer.create(this,uri);
         mediaPlayer.start();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });
         pause.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mediaPlayer.isPlaying()){
                     pause.setImageResource((R.drawable.play));
                     mediaPlayer.pause();
                 }
                 else{
                     pause.setImageResource(R.drawable.pause);
                     mediaPlayer.start();
                 }
             }
         });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                 if(position!=0){
                     position=position-1;
                 }
                 else{
                     position= songs.size()-1;
                 }
                Uri uri=Uri.parse((songs.get(position).toString()));
                mediaPlayer=mediaPlayer.create(MainActivity2.this,uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                pause.setImageResource(R.drawable.pause);
                songNme=songs.get(position).getName().toString();
                textView.setText(songNme);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!= songs.size()-1){
                    position=position+1;
                }
                else{
                    position= 0;
                }
                Uri uri=Uri.parse((songs.get(position).toString()));
                mediaPlayer=mediaPlayer.create(MainActivity2.this,uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                pause.setImageResource(R.drawable.pause);
                songNme=songs.get(position).getName().toString();
                textView.setText(songNme);

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        seekbarUpdate=new Thread(){
            public void run(){
                int currentPosition=0;
                try{
                    while(currentPosition <mediaPlayer.getDuration()){
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(600);
                    }
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
        };seekbarUpdate.start();
    }
}