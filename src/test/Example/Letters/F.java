package test.Example.Letters;

import DIMechanism.Annotations.Component;
import DIMechanism.Annotations.Lazy;
import DIMechanism.Annotations.PostConstruct;

@Lazy
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

    @PostConstruct
    public void postConstructMethod(){
        System.out.println("Obiectul F a fost creat");
    }
}
