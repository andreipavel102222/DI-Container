package org.example.DIMechanism.Container.PreDestroyTestComponents;

import org.example.DIMechanism.Annotations.Component;
import org.example.DIMechanism.Annotations.PreDestroy;

@Component
public class PreDestroyComponent {
    public static boolean preDestroyCalled = false;

    @PreDestroy
    public void preDestroyMethod(){
        preDestroyCalled = true;
    }

}
