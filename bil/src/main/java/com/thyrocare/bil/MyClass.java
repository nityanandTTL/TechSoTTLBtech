package com.thyrocare.bil;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class MyClass {

    public static void main(String[] args) {

        String str = "anananaam";
        int dup = 3;
//        char str1[] = str.toCharArray();
        int n = str.length();

        System.out.println(removeduplicate(str));

    }

    private static String removeduplicate(String str) {

        //Create LinkedHashSet of type character
        LinkedHashSet<Character> set = new LinkedHashSet<>();
        //Add each character of the string into LinkedHashSet
        for (int i = 0; i < str.length(); i++)
            set.add(str.charAt(i));

        // print the string after removing duplicate characters
        for (Character ch : set)
            System.out.print(ch);


        return str;
    }
}