package com.akshar.store.ui.sellersection.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by cedcoss on 6/4/16.
 */
public class Ced_MapComparator implements Comparator<Map<String, String>>
{
    private final String key;

    public Ced_MapComparator(String key)
    {
        this.key = key;
    }

    public int compare(Map<String, String> first, Map<String, String> second)
    {
        // TODO: Null checking, both for maps and values
        String firstValue = first.get(key);
        String secondValue = second.get(key);
        return firstValue.compareTo(secondValue);
    }
}
