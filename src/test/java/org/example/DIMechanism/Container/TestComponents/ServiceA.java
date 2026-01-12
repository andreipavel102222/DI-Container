package org.example.DIMechanism.Container.TestComponents;

import org.example.DIMechanism.Annotations.Component;

@Component
public class ServiceA {
    private final ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public ServiceB getServiceB() {
        return serviceB;
    }

    public String getServiceName() {
        return "ServiceA";
    }

    public String getDependencyName() {
        return serviceB.getName();
    }
}
