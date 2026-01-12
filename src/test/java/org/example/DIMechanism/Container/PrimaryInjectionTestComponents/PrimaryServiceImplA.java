package org.example.DIMechanism.Container.PrimaryInjectionTestComponents;

import org.example.DIMechanism.Annotations.Component;

@Component
public class PrimaryServiceImplA implements PrimaryService {
    @Override
    public String getService() {
        return "ServiceImplA";
    }
}
