package com.DIMechanism.Example.Letters;

import com.DIMechanism.Annotations.Component;

@Component
public class B {
    private A a;

    public B (A a){
        this.a = a;
    }

    public void printB() {
        System.out.println("bbbb");
    }

    public void printAfromB(){
        System.out.println("printing A from B");
        a.printA();
    }
}
