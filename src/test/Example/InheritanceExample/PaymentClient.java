package test.Example.InheritanceExample;

import DIMechanism.Annotations.Component;
import DIMechanism.Annotations.PreDestroy;
import DIMechanism.Annotations.Qualifier;

@Component
public class PaymentClient {
    private final PaymentService paymentService;

    public PaymentClient(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    public void payCheck(){
        paymentService.pay();
    }

    @PreDestroy
    public void preDestroyMethod() {
        System.out.println("Objectul PaymentClient se elimina");
    }
}
