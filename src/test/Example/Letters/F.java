package test.Example.Letters;

import DIMechanism.Annotations.Component;

@Component
public class F {
    public E e;

    public F(E e){
        this.e = e;
    }

    public void printF(){
        System.out.println("ffff");
    }

    public void printEfromF(){
        System.out.println("Print E from F");
        e.printE();
    }
}
