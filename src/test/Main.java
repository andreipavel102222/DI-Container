package test;

import DIMechanism.DIContainer.Container;
import test.Example.Letters.E;
import test.Example.Letters.F;


public class Main {
    public static void main(String[] args) {
        Container container = new Container(Config.class);
        E e = container.getComponent(E.class);
        E e2 = container.getComponent(E.class);

        System.out.println(e == e2);

        System.out.println(container.getComponents().size());
        System.out.println(container.getComponents().get("test.Example.Letters.E"));

        F f = container.getComponent(F.class);
        F f2 = container.getComponent(F.class);

        System.out.println(f.e == e);
        System.out.println(f2.e == e);
        System.out.println(f.e == f2.e);

        container.close();
    }
}