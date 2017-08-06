package ru.squel.workout;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Таймер, который перестает считать, когда не видим
 */

public class StopWatchFragment extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = StopWatchFragment.class.getSimpleName();

    private Handler mHandler = new Handler();
    private ru.squel.workout.SimpleTimer simpleTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleTimer = new SimpleTimer();

        if (savedInstanceState != null) {
            simpleTimer.setTime(savedInstanceState.getInt("timeInSeconds"));
            simpleTimer.setState(savedInstanceState.getBoolean("timerStatus"));
            simpleTimer.setWasRunning(savedInstanceState.getBoolean("timerRunning"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View layout = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        SeekBar seekbar = (SeekBar)layout.findViewById(R.id.seekBar);
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

        runTimer(layout);

        Button startButton = (Button)layout.findViewById(R.id.buttonStart);
        startButton.setOnClickListener(this);
        Button stopButton = (Button)layout.findViewById(R.id.buttonStop);
        stopButton.setOnClickListener(this);
        Button resetButton = (Button)layout.findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop()");
    }

    /**
     * При переключении фокуса на другое приложение или сворачивании этого
     */
    @Override
    public void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause()");

        simpleTimer.setWasRunning(simpleTimer.getState());
        simpleTimer.setState(false);
    }

    /**
     * При получении фокуса обратно
     */
    @Override
    public void onResume(){
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
    public void onSaveInstanceState(Bundle outData) {
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
    private void runTimer(View view) {
        final TextView timeView = (TextView) view.findViewById(R.id.textTime);

        mHandler.post(new Runnable() {
                          @Override
                          public void run() {
                              timeView.setText(simpleTimer.getFormattedText());
                              mHandler.postDelayed(this, simpleTimer.getstepSize());
                         }
                      }
        );
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.buttonStart:
                onClickStart(v);
                break;
            case R.id.buttonStop:
                onClickStop(v);
                break;
            case R.id.buttonReset:
                onClickReset(v);
                break;
            default:break;
        }
    }
}
