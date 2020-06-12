package controller;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main1(String[] args) {
        Short i1 = 5;
        Short i2 = 5;
        Short i3 = 150;
        Short i4 = 150;
        System.out.println(i1==i2);
        System.out.println(i3==i4);
    }

    public static void main(String[] args) {
        String a = "";
        List<Integer> excursionIds = Arrays.stream(a.split(","))
                .filter(s -> s.length() > 0)
                .map(Integer::parseInt)
                .collect(toList());
        System.out.println(excursionIds);
    }
}
