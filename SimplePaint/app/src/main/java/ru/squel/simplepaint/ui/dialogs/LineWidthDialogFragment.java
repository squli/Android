package ru.squel.simplepaint.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import ru.squel.simplepaint.MainActivityFragment;
import ru.squel.simplepaint.R;
import ru.squel.simplepaint.ui.PaintView;

/**
 * Created by sq on 29.07.2017.
 * субкласс DialogFragment, отображаемый командой меню для выбора толщины линии.
 */
public class LineWidthDialogFragment extends DialogFragment {

    private ImageView widthImageView;

    // Создает и возвращает AlertDialog
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
    // Создание диалогового окна
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View lineWidthDialogView =
                getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_line_width, null);
        builder.setView(lineWidthDialogView); // Добавление GUI

        // Назначение сообщения AlertDialog
        builder.setTitle(R.string.title_line_width_dialog);

        // Получение ImageView
        widthImageView = (ImageView) lineWidthDialogView.findViewById(
                R.id.widthImageView);

        // Настройка widthSeekBar
        final PaintView doodleView = getDoodleFragment().getPaintView();
        final SeekBar widthSeekBar = (SeekBar)
                lineWidthDialogView.findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(lineWidthChanged);
        widthSeekBar.setProgress(doodleView.getLineWidth());

    // Добавление кнопки Set Line Width
        builder.setPositiveButton(R.string.button_set_lone_width,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doodleView.setLineWidth(widthSeekBar.getProgress());
                    }
                }
        );

        return builder.create(); // Возвращение диалогового окна
    }

    // Возвращает ссылку на MainActivityFragment
    private MainActivityFragment getDoodleFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(
                R.id.paintFragment);
    }

    // Сообщает MainActivityFragment, что диалоговое окно находится на экране
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivityFragment fragment = getDoodleFragment();

        if (fragment != null)
            fragment.setDialogOnScreen(true);
    }

    // Сообщает MainActivityFragment, что окно не отображается
    @Override
    public void onDetach() {
        super.onDetach();
        MainActivityFragment fragment = getDoodleFragment();

        if (fragment != null)
            fragment.setDialogOnScreen(false);
    }

    // OnSeekBarChangeListener для SeekBar в диалоговом окне толщины линии
    private final SeekBar.OnSeekBarChangeListener lineWidthChanged =
            new SeekBar.OnSeekBarChangeListener() {
                final Bitmap bitmap = Bitmap.createBitmap(
                        400, 100, Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(bitmap); // Рисует на Bitmap

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    // Настройка объекта Paint для текущего значения SeekBar
                    Paint p = new Paint();
                    p.setColor(
                            getDoodleFragment().getPaintView().getDrawingColor());
                    p.setStrokeCap(Paint.Cap.ROUND);
                    p.setStrokeWidth(progress);

                    // Стирание объекта Bitmap и перерисовка линии
                    bitmap.eraseColor(
                            getResources().getColor(android.R.color.transparent,
                                    getContext().getTheme()));
                    canvas.drawLine(30, 50, 370, 50, p);
                    widthImageView.setImageBitmap(bitmap);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {} //
                //Обязательный метод

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {} //
                //Обязательный метод
            };
}
