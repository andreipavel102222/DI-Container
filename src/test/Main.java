package test;

import DIMechanism.DIContainer.Container;
import test.Example.InheritanceExample.PaymentClient;
import test.Example.Letters.C;
import test.Example.Letters.D;
import test.Example.Letters.E;


public class Main {
    public static void main(String[] args) {
        Container container = new Container(Config.class);

        System.out.println(container.getComponents());
        System.out.println(container.getComponentsMetadata());
        System.out.println(container.getComponentsInCreation());

        PaymentClient paymentClient = container.getComponent(PaymentClient.class);
        paymentClient.payCheck();

        C c = container.getComponent(C.class);

        c.printC();
        c.printBFromC();
        c.printAFromC();

        System.out.println(container.getComponents().size());
        System.out.println(container.getComponents().get("test.Example.Letters.D"));

        D d = container.getComponent(D.class);
        d.printD();

        System.out.println(container.getComponents().size());
        System.out.println(container.getComponents().get("test.Example.Letters.D"));

//        E e = container.getComponent(E.class);
//        e.printE();
//        e.printDfromE();
//        System.out.println(container.getComponents().size());
//        System.out.println(container.getComponents().get("test.Example.Letters.D"));
    }
}