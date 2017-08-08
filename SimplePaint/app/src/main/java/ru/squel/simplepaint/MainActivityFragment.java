package ru.squel.simplepaint;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.squel.simplepaint.ui.PaintView;
import ru.squel.simplepaint.ui.dialogs.ColorDialogFragment;
import ru.squel.simplepaint.ui.dialogs.EraseImageDialogFragment;
import ru.squel.simplepaint.ui.dialogs.LineWidthDialogFragment;

/**
 * A placeholder fragment containing a simple view.
 * управляет PaintView и обработкой событий акселерометра.
 */
public class MainActivityFragment extends Fragment {

    private PaintView paintView; // Обработка событий касания и рисования
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false;

    private String lastSavedImage = "";

    // Используется для обнаружения встряхивания устройства
    private static final int ACCELERATION_THRESHOLD = 100000;

    // Используется для идентификации запросов на использование
    // внешнего хранилища; необходимо для работы функции сохранения
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

    public MainActivityFragment() {
    }


    // Вызывается при создании представления фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        /// Т.к. у фрагмента имеются комнды панели действий, то
        setHasOptionsMenu(true);

        //ссылка на paintview
        paintView = (PaintView) view.findViewById(R.id.paintView);
        acceleration = 0.00f;
        // инициализация ускорением совбодного падения
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;

        return view;
    }

    /**
     * Начало прослушивания событтий датчика - только если на экране
     */
    @Override
    public void onResume() {
        super.onResume();
        enableAccelerometerListening();
    }

    public void onPause() {
        super.onPause();
        disableAccelerometerListening();
    }

    /// включение прослушивания событий акселерометра
    private void enableAccelerometerListening() {
        // получение объекта SensorManager
        SensorManager sensorManager =
                (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // регистрация слушателя событий акселерометра
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /// выключение прослушивания событий акселерометра
    private void disableAccelerometerListening() {
        SensorManager sensorManager =
                (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Прекращение прослушивания событий акселерометра
        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    /// обработчик событий акселерометра
    private final SensorEventListener sensorEventListener = new SensorEventListener() {

        /// проверка было ли событие встряхиванием
        @Override
        public void onSensorChanged(SensorEvent event) {
            /// На экране не было других диалоговых окон
            if (!dialogOnScreen) {
                // Получить значения x, y и z для SensorEvent
                float x = event.values[0] ;
                float y = event.values[1];
                float z = event.values[2];

                // Сохранить предыдущие данные ускорения
                lastAcceleration = currentAcceleration;

                // Вычислить текущее ускорение
                currentAcceleration = x * x + y * y + z * z;

                // Вычислить изменение ускорения
                acceleration = currentAcceleration *
                        (currentAcceleration - lastAcceleration);

                // Если изменение превышает заданный порог
                if (acceleration > ACCELERATION_THRESHOLD)
                    confirmErase();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void confirmErase() {
        EraseImageDialogFragment fragment = new EraseImageDialogFragment();
        fragment.show(getFragmentManager(), "erase dialog");
    }

    // Отображение команд меню фрагмента
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.paint_fragment_menu, menu);
    }

    // Обработка выбора команд меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Выбор в зависимости от идентификатора MenuItem
        switch (item.getItemId()) {
            case R.id.color:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.show(getFragmentManager(), "color dialog");
                return true; // Событие меню обработано
            case R.id.line_width:
                LineWidthDialogFragment widthDialog =
                        new LineWidthDialogFragment();
                widthDialog.show(getFragmentManager(), "line width dialog");
                return true; // Событие меню обработано
            case R.id.delete_drawing:
                confirmErase(); // Получить подтверждение перед стиранием
                return true; // Событие меню обработано
            case R.id.save:
                saveImage(); // Проверить разрешение и сохранить рисунок
                return true; // Событие меню обработано
            case R.id.print:
                paintView.printImage(); // Напечатать текущий рисунок
                return true; // Событие меню обработано
            }

        return super.onOptionsItemSelected(item);
        }

    /**
     * Метод запрашивает разрешение и сохраняет картинку
     */
    public void saveImage() {
        // Проверить, есть ли у приложения разрешение,
        // необходимое для сохранения
        if (getContext().checkSelfPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            // объяснить зачем понадобилось разрешение
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //назначить сообщение диалогу
                builder.setMessage(R.string.permission_explanation);

                //добавить кнопку ОК
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // запросить разрешение
                                requestPermissions(new String[] {
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                                }
                            }
                        );
                // отображение диалогового окна
                builder.create().show();
            }
            else {
                // Запросить разрешение
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SAVE_IMAGE_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            //разрешение уже было дано ранее
            lastSavedImage = paintView.saveImage();
        }
    }

    // Вызывается системой, когда пользователь предоставляет
    // или отклоняет разрешение для сохранения изображения
    @Override
    public void onRequestPermissionsResult(int requestCode,
        String[] permissions, int[] grantResults) {
        // switch выбирает действие в зависимости от того,
        // какое разрешение было запрошено
        switch (requestCode) {
            case SAVE_IMAGE_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    lastSavedImage = paintView.saveImage(); // Сохранить изображение
                }
                return;
        }
    }

    //метод возвращает объект PaintView
    public PaintView getPaintView() {
        return paintView;
    }

    // Проверка отображения диалогового окна в данный момент
    public void setDialogOnScreen (boolean visible) {
        dialogOnScreen = visible;
    }

    //получить название последнего сохраненного файла
    public String getLastSavedImage() {
        return lastSavedImage;
    }
}
