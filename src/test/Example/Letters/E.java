package test.Example.Letters;

import DIMechanism.Annotations.Component;
import DIMechanism.Annotations.PostConstruct;
import DIMechanism.Annotations.Scope;

@Scope("prototype")
@Component
public class E {
    public void printE(){
        System.out.println("eeee");
    }

    @PostConstruct
    public void postConstructMethod(){
        System.out.println("Obiectul E a fost creat");
    }
}
