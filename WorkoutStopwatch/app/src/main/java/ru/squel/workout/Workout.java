package ru.squel.workout;

import java.util.ArrayList;

/**
 * Created by sq on 21.07.2017.
 */
public class Workout {
    private String name;
    private String description;
    private static boolean isFilled = false;

    public Workout(String n, String dn) {
        this.name = n;
        this.description = dn;
    }

    public static ArrayList<Workout> workouts = new ArrayList<>();

    public static void fillValues() {
        if (!isFilled) {
            workouts.add(new Workout("The Limb Loosener",
                    "5 Handstand push-ups\n10 1-legged squats\n15 Pull-ups"));
            workouts.add(new Workout("Core Agony",
                    "100 Pull-ups\n100 Push-ups\n100 Sit-ups\n100 Squats"));
            workouts.add(new Workout("The Wimp Special",
                    "5 Pull-ups\n10 Push-ups\n15 Squats"));
            workouts.add(new Workout("Strength and Length", "500 meter run\n21 x 1.5 pood " +
                    "kettleball swing\n21 x pull-ups"));
            isFilled = true;
        }
    };

    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
