package org.example.DIMechanism.Container;

import org.example.DIMechanism.Container.CircularDependencyTestComponents.CircularConfig;
import org.example.DIMechanism.Container.InterfaceInjectionTestComponents.InterfaceConfig;
import org.example.DIMechanism.Container.InterfaceInjectionTestComponents.Service;
import org.example.DIMechanism.Container.LazyTestComponents.LazyConfig;
import org.example.DIMechanism.Container.LazyTestComponents.LazySingleton;
import org.example.DIMechanism.Container.PostConstructTestComponents.PostConstructComponent;
import org.example.DIMechanism.Container.PostConstructTestComponents.PostConstructConfig;
import org.example.DIMechanism.Container.PreDestroyTestComponents.PreDestroyComponent;
import org.example.DIMechanism.Container.PreDestroyTestComponents.PreDestroyConfig;
import org.example.DIMechanism.Container.PrimaryInjectionTestComponents.PrimaryInterfaceConfig;
import org.example.DIMechanism.Container.PrimaryInjectionTestComponents.PrimaryService;
import org.example.DIMechanism.Container.QualifierInjectionTestComponent.QualifierClient;
import org.example.DIMechanism.Container.QualifierInjectionTestComponent.QualifierConfig;
import org.example.DIMechanism.Container.TestComponents.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {
    @Test
    void testSingletonComponent(){
        Container container = new Container(Config.class);
        SingletonComponent singleton1 = container.getComponent(SingletonComponent.class);
        SingletonComponent singleton2 = container.getComponent(SingletonComponent.class);

        assertSame(singleton1, singleton2);
    }

    @Test
    void testPrototypeComponent(){
        Container container = new Container(Config.class);
        PrototypeComponent prototypeComponent1 = container.getComponent(PrototypeComponent.class);
        PrototypeComponent prototypeComponent2 = container.getComponent(PrototypeComponent.class);

        assertNotSame(prototypeComponent1, prototypeComponent2);
    }

    @Test
    void testNoComponentExceptionIsThrown(){
        Container container = new Container(Config.class);

        RuntimeException NoComponentException = assertThrows(RuntimeException.class, () -> {
           container.getComponent(NotComponent.class);
        });

        assertTrue(NoComponentException.getMessage().contains("No component was found for"));
    }

    @Test
    void testLazyComponent(){
        Container container = new Container(LazyConfig.class);
        assertFalse(LazySingleton.isInstantiated);

        container.getComponent(LazySingleton.class);

        assertTrue(LazySingleton.isInstantiated);
    }

    @Test
    void testDI(){
        Container container = new Container(Config.class);
        ServiceA serviceA = container.getComponent(ServiceA.class);
        ServiceB serviceB = container.getComponent(ServiceB.class);

        assertEquals("ServiceA", serviceA.getServiceName());
        assertEquals("ServiceB", serviceA.getDependencyName());

        assertSame(serviceB, serviceA.getServiceB());
    }

    @Test
    void testCircularDependencyExceptionIsThrown(){
        RuntimeException circularDependencyException = assertThrows(RuntimeException.class, () -> {
            new Container(CircularConfig.class);
        });

        assertTrue(circularDependencyException.getMessage().contains("Circular dependencies in component org.example.DIMechanism.Container.CircularDependencyTestComponents"));
    }

    @Test
    void testMultipleComponentsExceptionIsThrown(){
        Container container = new Container(InterfaceConfig.class);
        RuntimeException MultipleComponentsException = assertThrows(RuntimeException.class, () -> {
            container.getComponent(Service.class);
        });

        assertTrue(MultipleComponentsException.getMessage().contains("Multiple components for the type "));
    }

    @Test
    void testPrimaryImplementation(){
        Container container = new Container(PrimaryInterfaceConfig.class);
        PrimaryService primaryService = container.getComponent(PrimaryService.class);

        assertEquals("ServiceImplB", primaryService.getService());
    }

    @Test
    void testQualifierImplementation(){
        Container container = new Container(QualifierConfig.class);
        QualifierClient qualifierClient = container.getComponent(QualifierClient.class);

        assertEquals("ServiceImplB", qualifierClient.getService());
    }

    @Test
    void testPostConstructMethod(){
        assertFalse(PostConstructComponent.postConstructCalled);

        new Container(PostConstructConfig.class);

        assertTrue(PostConstructComponent.postConstructCalled);
    }

    @Test
    void testPreDestroyMethod(){
        Container container = new Container(PreDestroyConfig.class);

        assertFalse(PreDestroyComponent.preDestroyCalled);

        container.close();

        assertTrue(PreDestroyComponent.preDestroyCalled);
    }
}
