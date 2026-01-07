package test.Example.InheritanceExample;

import DIMechanism.Annotations.Component;

@Component
public class CardPaymentService implements PaymentService{
    @Override
    public void pay() {
        System.out.println("pay with credit card");
    }
}
