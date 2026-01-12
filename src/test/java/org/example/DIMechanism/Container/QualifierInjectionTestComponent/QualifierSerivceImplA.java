package org.example.DIMechanism.Container.QualifierInjectionTestComponent;

import org.example.DIMechanism.Annotations.Component;

@Component
public class QualifierSerivceImplA implements QualifierService{
    @Override
    public String getService() {
        return "SerivceImplA";
    }
}
