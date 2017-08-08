package ru.squel.simplepaint.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.squel.simplepaint.MainActivityFragment;
import ru.squel.simplepaint.R;

/**
 * Created by sq on 29.07.2017.
 * субкласс DialogFragment, отображаемый командой меню для стирания или встряхиванием устройства для стирания текущего рисунка.
 */
public class EraseImageDialogFragment extends DialogFragment {

    // Создание и возвращение объекта AlertDialog
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        // Назначение сообщения AlertDialog
        builder.setMessage(R.string.message_erase);

        // Добавление кнопки стирания
        builder.setPositiveButton(R.string.button_erase,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDoodleFragment().getPaintView().clear(); // Очистка
                    }
                }
        );

        // Добавление кнопки стирания
        builder.setNegativeButton( android.R.string.cancel, null);
        return builder.create(); // Возвращает диалоговое окно
    }

    // Получение ссылки на MainActivityFragment
    private MainActivityFragment getDoodleFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(
                R.id.paintFragment);
    }

    // Сообщает MainActivityFragment, что окно находится на экране
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
}
