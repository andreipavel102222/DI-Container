package com.DIMechanism;

import com.DIMechanism.DIContainer.Container;
import com.DIMechanism.Example.Animals.Cat;
import com.DIMechanism.Example.InheritanceExample.CardPaymentService;
import com.DIMechanism.Example.InheritanceExample.PaymentClient;
import com.DIMechanism.Example.InheritanceExample.PaymentService;
import com.DIMechanism.Example.Letters.A;
import com.DIMechanism.Example.Letters.B;
import com.DIMechanism.Example.Letters.C;
import com.DIMechanism.Example.Square;


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