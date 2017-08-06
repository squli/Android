package ru.squel.starbuzz1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/*
    Не нужно строить макет самостоятельно
    Не нужно реализовывать собственного слушателя
 */
public class DrinkCategoryActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создается адаптер
        ArrayAdapter<Drink> listAdapter = new ArrayAdapter<Drink>(
                this,   //текущая активнсоть
                android.R.layout.simple_list_item_1,    //встроенный ресурс макета,
                                                    // отображает каждый элемент массива в виде надписи
                Drink.getDrinksAll());   // непосредственно массив

        // получаю элемент листвью из текущей активности,
        // он есть т.к. активность расширяет класс ListActivity
        ListView listDrinks = getListView();
        // подключаю адаптер
        listDrinks.setAdapter(listAdapter);
    }


    /**
     * Т.к. ListActivity уже содержит реализацию щелчка по списку, его нужно перелпределить
     */
    @Override
    public void onListItemClick(ListView listView,
                                View itemView,
                                int position,
                                long id) {
        Intent intent = new Intent(DrinkCategoryActivity.this, DrinkDetailActivity.class);
        intent.putExtra(DrinkDetailActivity.EXTRA_DRINKNO, (int) id);
        startActivity(intent);
    }
}
