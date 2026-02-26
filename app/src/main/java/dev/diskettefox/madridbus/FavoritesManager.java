package dev.diskettefox.madridbus;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesManager {
    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String KEY_FAVORITES = "favorite_stops";

    public static void addFavorite(Context context, String stopId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<>()));
        favorites.add(stopId);
        prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public static void removeFavorite(Context context, String stopId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<>()));
        favorites.remove(stopId);
        prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public static boolean isFavorite(Context context, String stopId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
        return favorites.contains(stopId);
    }

    public static List<Integer> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
        List<Integer> favoriteIds = new ArrayList<>();
        for (String id : favorites) {
            try {
                favoriteIds.add(Integer.parseInt(id));
            } catch (NumberFormatException ignored) {}
        }
        return favoriteIds;
    }
}