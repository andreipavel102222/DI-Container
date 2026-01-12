package org.example.DIMechanism.Container.CircularDependencyTestComponents;

import org.example.DIMechanism.Annotations.Component;

@Component
public class CircularDepA {
    private CircularDepB circularDepB;

    public CircularDepA(CircularDepB circularDepB){
        this.circularDepB = circularDepB;
    }
}
