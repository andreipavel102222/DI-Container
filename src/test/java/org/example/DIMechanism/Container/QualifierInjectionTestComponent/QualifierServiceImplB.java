package org.example.DIMechanism.Container.QualifierInjectionTestComponent;

import org.example.DIMechanism.Annotations.Component;

@Component
public class QualifierServiceImplB implements QualifierService{
    @Override
    public String getService() {
        return "ServiceImplB";
    }
}
