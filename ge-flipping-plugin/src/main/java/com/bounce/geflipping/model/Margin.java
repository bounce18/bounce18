package com.bounce.geflipping.model;

public class Margin
{
    public int high;
    public int low;

    public int profit()
    {
        return high - low;
    }
}
