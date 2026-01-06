package com.test;

import com.DIMechanism.DIContainer.Container;
import com.test.Example.InheritanceExample.PaymentClient;
import com.test.Example.Letters.C;


public class Main {
    public static void main(String[] args) {
        Container container = new Container(Config.class);

        System.out.println(container.getComponents());
        System.out.println(container.getComponentsMetadata());
        PaymentClient paymentClient = container.getComponent(PaymentClient.class);
        paymentClient.payCheck();

        C c = container.getComponent(C.class);

        c.printC();
        c.printBFromC();
        c.printAFromC();
    }
}