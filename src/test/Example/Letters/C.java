package test.Example.Letters;

import DIMechanism.Annotations.Component;

@Component
public class C {
    private final B b;

    public C(B b){
        this.b = b;
    }

    public void printC(){
        System.out.println("cccc");
    }

    public void printBFromC(){
        System.out.println("printing B from C");
        b.printB();
    }

    public void printAFromC(){
        System.out.println("printing A from C");
        b.printAfromB();
    }
}
