package test.Example.Letters;

import DIMechanism.Annotations.Component;
import DIMechanism.Annotations.Scope;

@Scope("prototype")
@Component
public class E {
    public void printE(){
        System.out.println("eeee");
    }
}
