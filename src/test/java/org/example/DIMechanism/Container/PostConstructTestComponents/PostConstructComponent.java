package org.example.DIMechanism.Container.PostConstructTestComponents;

import org.example.DIMechanism.Annotations.Component;
import org.example.DIMechanism.Annotations.PostConstruct;

@Component
public class PostConstructComponent {
    public static boolean postConstructCalled = false;

    @PostConstruct
    public void postConstructMethod(){
        postConstructCalled = true;
    }
}
