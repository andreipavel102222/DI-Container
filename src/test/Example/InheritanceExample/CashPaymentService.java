package test.Example.InheritanceExample;

import DIMechanism.Annotations.Component;

@Component
public class CashPaymentService implements PaymentService{
    @Override
    public void pay() {
        System.out.println("pay with cash");
    }
}
