package test.Example.Letters;

import DIMechanism.Annotations.Component;

public class E {
    private final D d;
    public E(D d){
        this.d = d;
    }

    public void printE(){
        System.out.println("eeee");
    }

    public void printDfromE(){
        d.printD();
    }
}
