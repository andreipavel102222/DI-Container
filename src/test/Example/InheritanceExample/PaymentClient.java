package test.Example.InheritanceExample;

import DIMechanism.Annotations.Component;
import DIMechanism.Annotations.Qualifier;

@Component
public class PaymentClient {
    private final PaymentService paymentService;

    public PaymentClient(@Qualifier("test.Example.InheritanceExample.CashPaymentService") PaymentService paymentService){
        this.paymentService = paymentService;
    }

    public void payCheck(){
        paymentService.pay();
    }
}
