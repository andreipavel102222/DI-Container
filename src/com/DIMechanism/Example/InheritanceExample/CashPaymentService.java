package com.DIMechanism.Example.InheritanceExample;

import com.DIMechanism.Annotations.Component;

@Component
public class CashPaymentService implements PaymentService{
    @Override
    public void pay() {
        System.out.println("pay with cash");
    }
}
