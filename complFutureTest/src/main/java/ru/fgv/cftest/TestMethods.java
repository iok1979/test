/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.fgv.cftest;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prusakovan
 */
public class TestMethods {
    public static List<Integer> fillNotOddList() {
        ArrayList<Integer> arrayList = new ArrayList<>(50);
        for (int i = 0; i < 100000; i++) {
            if (i % 2 == 0) {
                arrayList.add(i);
            }
        }
        return arrayList;
    }
    
    public static List<Integer> fillOddList() {
        ArrayList<Integer> arrayList = new ArrayList<>(50);
        for (int i = 0; i < 100000; i++) {
            if (i % 2 != 0) {
                arrayList.add(i);
            }
        }
        return arrayList;
    }
}
