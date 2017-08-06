package ru.squel.workout;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOutListFragment extends ListFragment {

    // интерфейс, который должна реализовывать активность, чтобы
    // получить вызовы из кода фрагмента
    static interface WorkoutListListener {
        void itemClicked(long id);
    }

    // ссылка на активность
    private WorkoutListListener listener;

    public WorkOutListFragment() {
        // Required empty public constructor
    }

    /**
     * Вызывается при присоединении фрагмента к активности
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (WorkoutListListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // создать список с одними названиями
        ArrayList<String> names = new ArrayList<>();
        Workout.fillValues();
        for (int i = 0; i < Workout.workouts.size(); ++i){
            names.add(Workout.workouts.get(i).getName());
        }

        // создать адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1, names);
        // связать адаптер со списковым представлением
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Переопределение слушателя нажатий
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }

}
