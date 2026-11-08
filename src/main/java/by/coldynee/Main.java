package by.coldynee;

import by.coldynee.homework_1.CustomHashMap;

public class Main {
    public static void main(String[] args) {
        //homework 1
        CustomHashMap<Integer, String> map = new CustomHashMap<>(10);
        map.put(1, "one");
        map.put(2, "two");
        System.out.println(map.get(1));
        System.out.println(map.get(2));
        map.remove(1);
        map.remove(2);
        System.out.println(map.get(1));
        System.out.println(map.get(2));
    }
}