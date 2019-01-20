//package com.oladapo.jukebox;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import com.sothree.slidinguppanel.SlidingUpPanelLayout;
//
//@SuppressLint("Registered")
//public class NowPlaying extends Activity {
//
//    ImageButton play, pause, play_main, pause_main;
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.now_playing);
//
//        play = findViewById(R.id.play_button);
//        pause = findViewById(R.id.pause_button);
//        play_main = findViewById(R.id.play_button_main);
//        pause_main = findViewById(R.id.pause_button_main);
//
//        SlidingUpPanelLayout now_playing = findViewById(R.id.slideup_nowplaying);
//
//        play.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                play.setVisibility(View.GONE);
//                pause.setVisibility(View.VISIBLE);
//
//                Toast.makeText(NowPlaying.this, "Song Is now Playing", Toast.LENGTH_SHORT).show();
//
//                if (play_main.getVisibility() == View.VISIBLE) {
//                    play_main.setVisibility(View.GONE);
//                    pause_main.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//
//        });
//
//        pause.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                pause.setVisibility(View.GONE);
//                play.setVisibility(View.VISIBLE);
//
//                Toast.makeText(NowPlaying.this, "Song is Pause", Toast.LENGTH_SHORT).show();
//
//                if (pause_main.getVisibility() == View.VISIBLE) {
//                    pause_main.setVisibility(View.GONE);
//                    play_main.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//
//        });
//
//        play_main.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                play_main.setVisibility(View.GONE);
//                pause_main.setVisibility(View.VISIBLE);
//
//                Toast.makeText(NowPlaying.this, "Song Is now Playing", Toast.LENGTH_SHORT).show();
//
//                if (play.getVisibility() == View.VISIBLE) {
//                    play.setVisibility(View.GONE);
//                    pause.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//
//        });
//
//        pause_main.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                pause_main.setVisibility(View.GONE);
//                play_main.setVisibility(View.VISIBLE);
//
//                Toast.makeText(NowPlaying.this, "Song is Pause", Toast.LENGTH_SHORT).show();
//
//                if (pause.getVisibility() == View.VISIBLE) {
//                    pause.setVisibility(View.GONE);
//                    play.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//
//        });
//
//
//    }
//}
