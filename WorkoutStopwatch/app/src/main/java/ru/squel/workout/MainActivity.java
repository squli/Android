package ru.squel.workout;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements WorkOutListFragment.WorkoutListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void itemClicked (long id) {

        // попробовать получить фрагмент, он есть только на планшете, поэтому
        // нужно пересоздать фрагмент во фрейме
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            WorkOutDetailFragment fragment = new WorkOutDetailFragment();
            // установить id для отображения в новом фрагменте
            fragment.setWorkOutId(id);
            // создать новую тразакцию
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // замена фрейма fragment_container на созданный фрагмент
            transaction.replace(R.id.fragment_container, fragment);
            // запуск транзакции
            transaction.addToBackStack(null);
            // настройка анимации при пересоздании фрагмента
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        } // если фрейма нет, то значит телефон и шлем интент новой активити
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_WORKOUT_ID, (int)id);
            startActivity(intent);
        }
    }

}
