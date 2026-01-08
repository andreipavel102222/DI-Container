package test.Example.InheritanceExample;

import DIMechanism.Annotations.Component;
import DIMechanism.Annotations.Primary;


@Component
public class CardPaymentService implements PaymentService{
    @Override
    public void pay() {
        System.out.println("pay with credit card");
    }
}
