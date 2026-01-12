package org.example.DIMechanism.Container.PrimaryInjectionTestComponents;

import org.example.DIMechanism.Annotations.Component;
import org.example.DIMechanism.Annotations.Primary;

@Primary
@Component
public class PrimaryServiceImplB implements PrimaryService {
    @Override
    public String getService() {
        return "ServiceImplB";
    }
}
