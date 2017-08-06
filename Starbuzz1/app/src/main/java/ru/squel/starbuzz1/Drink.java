package ru.squel.starbuzz1;

import java.util.ArrayList;

/**
 * Created by sq on 21.07.2017.
 * Описание всех возможных напитков
 */
public class Drink {
    private String name;
    private String description;
    private int imageResourceId;
    private static boolean filled = false;

    public static final ArrayList<Drink> drinksAll = new ArrayList<>();

    public Drink(String n, String d, int img) {
        name = n;
        description = d;
        imageResourceId = img;
    }

    public static void fillValues() {
        drinksAll.add(new Drink("Latte", "A couple of espresso shots with steamed milk", R.drawable.latte));
        drinksAll.add(new Drink("Cappuccino", "Espresso, hot milk, and a steamed milk foam", R.drawable.cappuccino));
        drinksAll.add(new Drink("Filter", "Highest quality beans roasted and brewed fresh", R.drawable.filter));
        filled = true;
    }

    public static ArrayList<Drink> getDrinksAll() {
        if (!filled)
            Drink.fillValues();
        return drinksAll;
    }

    public String getName() {return name;}
    public String getDescription() {return description;}
    public int getImageResourceId() {return imageResourceId;}

    public void getName(String n) {name = n;}
    public void getDescription(String d) {description = d;}
    public void getImageResourceId(int id) {imageResourceId = id;}

    @Override
    public String toString() {
        return this.name;
    }
}
