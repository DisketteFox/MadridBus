package dev.diskettefox.madridbus;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavoritesManager {
    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String KEY_FAVORITES = "favorite_stops_ordered";
    private static final String KEY_FAVORITE_NAMES="favorite_names";

    public static void addFavorite(Context context, String stopId) {
        List<Integer> favorites = getFavorites(context);
        int id;
        try {
            id = Integer.parseInt(stopId);
        } catch (NumberFormatException e) {
            return;
        }
        if (!favorites.contains(id)) {
            favorites.add(id);
            saveFavorites(context, favorites);
        }
    }
    public static void addFavoriteWithName(Context context, String stopId, String customName){
        addFavorite(context, stopId);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_FAVORITE_NAMES + stopId, customName).apply();
    }

    public static void removeFavorite(Context context, String stopId) {
        List<Integer> favorites = getFavorites(context);
        try {
            favorites.remove(Integer.valueOf(stopId));
        } catch (NumberFormatException ignored) {}
        saveFavorites(context, favorites);
    }

    public static boolean isFavorite(Context context, String stopId) {
        try {
            return getFavorites(context).contains(Integer.parseInt(stopId));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<Integer> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String favoritesString = prefs.getString(KEY_FAVORITES, "");
        List<Integer> favoriteIds = new ArrayList<>();
        if (!favoritesString.isEmpty()) {
            String[] ids = favoritesString.split(",");
            for (String id : ids) {
                try {
                    favoriteIds.add(Integer.parseInt(id));
                } catch (NumberFormatException ignored) {}
            }
        } else {
            Set<String> oldFavorites = prefs.getStringSet("favorite_stops", null);
            if (oldFavorites != null) {
                for (String id : oldFavorites) {
                    try {
                        favoriteIds.add(Integer.parseInt(id));
                    } catch (NumberFormatException ignored) {}
                }
                saveFavorites(context, favoriteIds);
            }
        }
        return favoriteIds;
    }
    public static String getFavoriteName(Context context, String stopId) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_FAVORITE_NAMES + stopId, null);
    }
    public static void saveFavorites(Context context, List<Integer> favoriteIds) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        List<String> stringIds = new ArrayList<>();
        for (Integer id : favoriteIds) {
            stringIds.add(String.valueOf(id));
        }
        prefs.edit().putString(KEY_FAVORITES, TextUtils.join(",", stringIds)).apply();
    }
}
