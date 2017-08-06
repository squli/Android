package ru.squel.workout;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOutDetailFragment extends Fragment {

    private long workOutId = 0;

    public WorkOutDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            workOutId = savedInstanceState.getLong("workoutId");
        }
        // создание фрагмента будет происходить только в первый раз, во всех остальных случаях
        // будет использоваться восстановленный по логу транзакций фрагмент
        else {
            ///создание вложенной транзакции для демонстрации таймера
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            StopWatchFragment stopWatchFragment = new StopWatchFragment();
            ft.replace(R.id.stopwatch_container, stopWatchFragment);
            ft.addToBackStack(null);

            //стиль анимации перехода
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // отправить транзакцию
            ft.commit();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_out_detail, container, false);
    }

    /**
     * При создании фрагмента будут заполнены поля представления по айди WorkOut
     */
    @Override
    public void onStart() {
        super.onStart();
        // получили корневой View фрагмента
        View view = getView();
        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.textTitle);
            Workout workout = Workout.workouts.get((int) workOutId);
            title.setText(workout.getName());
            TextView description = (TextView) view.findViewById(R.id.textDescription);
            description.setText(workout.getDescription());
        }
    }

    /**
     * Для присваивания идентификатора
     * @param id
     */
    public void setWorkOutId(long id) {
        this.workOutId = id;
    }

    /**
     * Для сохранения номера отображаемой детализации
     * @param savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("workoutId", workOutId);
    }
}
