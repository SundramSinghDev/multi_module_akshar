package com.akshar.store.ui.sellersection.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by cedcoss on 6/4/16.
 */
public class Ced_MapComprator_des implements Comparator<Map<String, String>>
{
    private final String key;

    public Ced_MapComprator_des(String key)
    {
        this.key = key;
    }

    public int compare(Map<String, String> first,
                       Map<String, String> second)
    {
        // TODO: Null checking, both for maps and values
        String firstValue = first.get(key);
        String secondValue = second.get(key);
        return secondValue.compareTo(firstValue);
    }
}