package com.DIMechanism.Example;

import com.DIMechanism.Annotations.Autowired;
import com.DIMechanism.Annotations.Component;


public class Square {
    public int l;

    public Square() {
        this.l = 0;
    }

    public Square(int l) {
        this.l = l;
    }

    public void printMessage() {
        System.out.println("This is a square");
    }
}
