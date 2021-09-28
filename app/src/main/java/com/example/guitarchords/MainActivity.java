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
    private int currentSound, gameProgress, points = 0, currentSizeList = 12;
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
        gameProgress = 0;
        updateRandomListSound(currentSizeList);
        currentSound = soundsLvl.get(gameProgress);
        txtPoints.setText("points : " + points);
        btnChangeLvl.setEnabled(false);
        playSound(currentSound + 1);
    }

    //изменение уровня сложности
    public void changeLvl(View v){
        String lvl = ((Button)v).getText().toString();
        for(int i = 0; i < 3; i++){
            if(lvl.equals(lvls[i])){
                if(i == 2) ((Button)v).setText(lvls[0]);
                else ((Button)v).setText(lvls[i + 1]);
            }
        }

        for (Button btn : buttons) {
            btn.setEnabled(true);
        }

        switch (((Button)v).getText().toString()){
            case "easy": {
                currentSizeList = 4;
                break;
            }
            case "normal": {
                currentSizeList = 8;
                break;
            } case "hard": {
                currentSizeList = 12;
                break;
            }
        }
    }

    private void updateRandomListSound(int size){
        soundsLvl.clear();
        for(int i = 0; i < size; i++){
            int rand = (int) (Math.random() * size);
            soundsLvl.add(rand);
        }
        for(int i = size; i < 12; i++)
            buttons[i].setEnabled(false);
    }


    //обработчик нажатия на кнопку
    public void btnClick(View v){
        String txt = ((Button) v).getTag().toString();
        if(currentSound == (Integer.parseInt(txt))){
            points++;
            txtPoints.setText("points : " + points);
        }
        if((soundsLvl.size() - 1) == gameProgress){
            // Toast.makeText(MainActivity.this, "вы набрали " + points + " очков", Toast.LENGTH_SHORT).show();
            btnChangeLvl.setEnabled(true);
            currentSound = -1;
        }else{
            gameProgress++;
            currentSound = soundsLvl.get(gameProgress);
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

        updateRandomListSound(currentSizeList);
    }
}