package org.example.DIMechanism.Container.CircularDependencyTestComponents;

import org.example.DIMechanism.Annotations.Component;

@Component
public class CircularDepB {
    private CircularDepA circularDepA;

    public CircularDepB(CircularDepA circularDepA){
        this.circularDepA = circularDepA;
    }
}
