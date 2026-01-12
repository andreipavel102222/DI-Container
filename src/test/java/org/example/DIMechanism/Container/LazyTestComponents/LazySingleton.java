package org.example.DIMechanism.Container.LazyTestComponents;

import org.example.DIMechanism.Annotations.Component;
import org.example.DIMechanism.Annotations.Lazy;

@Lazy
@Component
public class LazySingleton {
    public static boolean isInstantiated = false;

    public LazySingleton(){
        isInstantiated = true;
    }
}
