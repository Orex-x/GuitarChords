package com.example.guitarchords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private SoundPool mSoundPool;
    private TextView txtPoints;
    static Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11;
    static Button[] buttons = new Button[]{b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11};
    static int[] sounds = new int[] {
            R.raw.a, R.raw.c, R.raw.e, R.raw.d,R.raw.am, R.raw.dm,
            R.raw.em, R.raw.g,R.raw.a7,R.raw.g7,R.raw.e7,R.raw.c7
    };
    private int points = 0;
    private int currentSound, ii;
    private ArrayList<Integer> soundsLvl = new ArrayList<>();
    Button btnPlay, btnRepeat, btnChangeLvl;
    String[] lvls = new String[]{"easy", "normal", "hard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //играем игру
    public void play(View v){
        points = 0;
        ii = 0;
        currentSound = soundsLvl.get(ii);
        txtPoints.setText("Очки : " + points);
        btnChangeLvl.setEnabled(false);
        playSound(currentSound + 1);
    }

    //изменение уровня сложности
    public void changeLvl(View v){
        soundsLvl.clear();
        String lvl = ((Button)v).getText().toString();
        for(int i = 0; i < 3; i++){
            if(lvl.equals(lvls[i])){
                if(i == 2) ((Button)v).setText(lvls[0]);
                else {
                    ((Button)v).setText(lvls[i + 1]);
                }
            }
        }

        for (Button btn : buttons) {
            btn.setEnabled(true);
        }

        switch (((Button)v).getText().toString()){
            case "easy": {
                for(int i = 4; i < 12; i++){
                    buttons[i].setEnabled(false);
                }

                for(int i = 0; i < 4; i++){
                    int rand = (int) (Math.random() * 4);
                    soundsLvl.add(rand);
                }
                break;
            }
            case "normal": {
                for(int i = 8; i < 12; i++){
                    buttons[i].setEnabled(false);
                }
                for(int i = 0; i < 8; i++){
                    int rand = (int) (Math.random() * 8);
                    soundsLvl.add(rand);
                }
                break;
            } case "hard": {
                for(int i = 0; i < 12; i++){
                    int rand = (int) (Math.random() * 12);
                    soundsLvl.add(rand);
                }
                break;
            }
        }
    }


    //обработчик нажатия на кнопку
    public void btnClick(View v){

        String txt = ((Button) v).getTag().toString();
        if(currentSound == (Integer.parseInt(txt))){
            points++;
            txtPoints.setText("Очки : " + points);
        }
        if((soundsLvl.size() - 1) == ii){
            // Toast.makeText(MainActivity.this, "вы набрали " + points + " очков", Toast.LENGTH_SHORT).show();
            btnChangeLvl.setEnabled(true);
            currentSound = -1;
        }else{
            ii++;
            currentSound = soundsLvl.get(ii);
            playSound(currentSound + 1);
        }

    }


    public void btnClickRepeat(View v){
        playSound(currentSound + 1);
    }


/*
    //для свободного режима
    public void btnClickPlay(View v){
        String txt = ((Button) v).getTag().toString();
        playSound(Integer.parseInt(txt) + 1);
    }
*/


    public void playSound(int mSoundId){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int no_loop = 0;
        int priority = 1;
        float normal_playback_rate = 1f;
        mSoundPool.play(mSoundId, leftVolume, rightVolume, priority, no_loop,
                normal_playback_rate);
    }

    public void init(){
        txtPoints = findViewById(R.id.txtPoints);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this::play);

        btnRepeat = findViewById(R.id.btnRepeat);
        btnRepeat.setOnClickListener(this::btnClickRepeat);
        btnChangeLvl = findViewById(R.id.btnChangeLvl);
        btnChangeLvl.setOnClickListener(this::changeLvl);

        mSoundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 100);

        for(int i = 0; i < 12; i++){
            String buttonID = "b" + i;
            int resultID =  getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resultID);
            buttons[i].setTag(i);
            buttons[i].setOnClickListener(this::btnClick);
        }

        for (int idSound : sounds)
            mSoundPool.load(this, idSound, 1);

        for(int i = 0; i < 12; i++){
            int rand = (int) (Math.random() * 12);
            soundsLvl.add(rand);
        }
    }
}