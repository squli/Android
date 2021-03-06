package ru.squel.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Таймер, который перестает считать, когда не видим
 */

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Handler mHandler = new Handler();
    private SimpleTimer simpleTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleTimer = new SimpleTimer();

        if (savedInstanceState != null) {
            simpleTimer.setTime(savedInstanceState.getInt("timeInSeconds"));
            simpleTimer.setState(savedInstanceState.getBoolean("timerStatus"));
            simpleTimer.setWasRunning(savedInstanceState.getBoolean("timerRunning"));
        }
        runTimer();

        SeekBar seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int stepOfTimer_ms = 100 + progress;
                simpleTimer.setStepSize(stepOfTimer_ms);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop()");
    }

    /**
     * При переключении фокуса на другое приложение или сворачивании этого
     */
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause()");

        simpleTimer.setWasRunning(simpleTimer.getState());
        simpleTimer.setState(false);
    }

    /**
     * При получении фокуса обратно
     */
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume()");

        // был запущен в момоент вызова паузы
        if (simpleTimer.getWasRunning() == true)
            simpleTimer.setState(true);
    }

    /**
     * Для сохранения данных приложения при переключении фокуса/получении обратно
     * @param outData
     */
    @Override
    protected void onSaveInstanceState(Bundle outData) {
        super.onSaveInstanceState(outData);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        outData.putInt("timeInSeconds", simpleTimer.getSeconds());
        outData.putBoolean("timerStatus", simpleTimer.getState());
        outData.putBoolean("timerRunning", simpleTimer.getWasRunning());
    }

    /**
     * Обработчики кнопок
      */
    public void onClickStart(View view) {
        simpleTimer.setState(true);
    }

    /**
     * Обработчики кнопок
     */
    public void onClickStop(View view) {
        simpleTimer.setState(false);
    }

    /**
     * Обработчики кнопок
     */
    public void onClickReset(View view) {
        simpleTimer.setTime(0);
    }

    /**
     * Будет запускаться каждую stepOfTimer_ms мс заново
     */
    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textTime);

        mHandler.post(new Runnable() {
                          @Override
                          public void run() {
                              timeView.setText(simpleTimer.getFormattedText());
                              mHandler.postDelayed(this, simpleTimer.getstepSize());
                         }
                      }
        );
    }


}
