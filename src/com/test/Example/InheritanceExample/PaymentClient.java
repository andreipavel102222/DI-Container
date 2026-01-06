package com.test.Example.InheritanceExample;

import com.DIMechanism.Annotations.Component;
import com.DIMechanism.Annotations.Qualifier;

@Component
public class PaymentClient {
    private final PaymentService paymentService;

    public PaymentClient(@Qualifier("com.test.Example.InheritanceExample.CashPaymentService") PaymentService paymentService){
        this.paymentService = paymentService;
    }

    public void payCheck(){
        paymentService.pay();
    }
}
