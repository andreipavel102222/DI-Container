package org.example.DIMechanism.Container.QualifierInjectionTestComponent;

import org.example.DIMechanism.Annotations.Component;
import org.example.DIMechanism.Annotations.Qualifier;

@Component
public class QualifierClient {
    private final QualifierService qualifierService;

    public QualifierClient(@Qualifier("org.example.DIMechanism.Container.QualifierInjectionTestComponent.QualifierServiceImplB") QualifierService qualifierService){
        this.qualifierService = qualifierService;
    }

    public String getService(){
        return qualifierService.getService();
    }
}
