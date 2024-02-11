package com.example.foodplanner.home.foryou.view.area;

import com.example.foodplanner.R;

import java.util.HashMap;

public class CountryImageMap {
    private static final HashMap<String, Integer> countryImageMap = new HashMap<String, Integer>() {{
        put("American", R.drawable.united_states);
        put("British", R.drawable.united_kingdom);
        put("Canadian", R.drawable.canada);
        put("Chinese", R.drawable.china);
        put("Croatian", R.drawable.croatia);
        put("Dutch", R.drawable.netherlands);
        put("Egyptian", R.drawable.egypt);
        put("Filipino", R.drawable.philippines);
        put("French", R.drawable.france);
        put("Greek", R.drawable.greece);
        put("Indian", R.drawable.india);
        put("Irish", R.drawable.ireland);
        put("Italian", R.drawable.italy);
        put("Jamaican", R.drawable.jamaica);
        put("Japanese", R.drawable.japan);
        put("Kenyan", R.drawable.kenya);
        put("Malaysian", R.drawable.malaysia);
        put("Mexican", R.drawable.mexico);
        put("Moroccan", R.drawable.morocco);
        put("Polish", R.drawable.poland);
        put("Portuguese", R.drawable.portugal);
        put("Russian", R.drawable.russia);
        put("Spanish", R.drawable.spain);
        put("Thai", R.drawable.thailand);
        put("Tunisian", R.drawable.tunisia);
        put("Turkish", R.drawable.turkey);
        put("Unknown", R.drawable.unknown);
        put("Vietnamese", R.drawable.vietnam);
    }};

    public static int getCountryImageResource(String country) {
        Integer resource = countryImageMap.get(country);
        if (resource == null) {
            return R.drawable.unknown;
        }
        return resource;
    }
}
