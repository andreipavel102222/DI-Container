# Java Dependency Injection Container

A lightweight Dependency Injection container inspired by Spring, built from scratch
using Java Reflection. This project was created to deeply understand how frameworks
like Spring works internally.

## Features

The container currently supports:

- **Component scanning**
- **Constructor-based dependency injection**
- **Scopes**
    - Singleton
    - Prototype
- **Lazy and eager initialization**
- **Interface injection**
- **@Primary and @Qualifier support**
- **Circular dependency detection**
- **Lifecycle hooks**
    - @PostConstruct
    - @PreDestroy

<br>

## Design Principles

The container is built around a clean separation of responsibilities.

| Component           | Responsability                                                      |
|---------------------|---------------------------------------------------------------------|
| Container           | Public API. Entry point for creating and retrieving components.     |
| ComponentFactory    | Creates and caches component instances                              |
| ComponentMetadata   | Extracts reflection-based information (scope, lazy, lifecycle,etc.) |
| Dependency Resolver | Extracts constructor dependencies                                   |
| Scanner             | Discovers component classes in the classpath                        |

<br>

## How it works
1. A ```Container``` is created with a configuration class.
2. The scanner scans the package of the config class for components.
3. All eager singletons are instantiated immediately.
4. When a component is created:
    - The correct constructor is selected (```@Autowired``` if present)
    - Dependencies are resolved
    - Circular dependencies are detected
    - ```@PostConstruct``` is called
5. When ```container.close()``` is called all singleton components receive ```@PreDestroy```

<br>

## Scopes

| Scope          | Behavior                                   |
|----------------|--------------------------------------------|
| Singleton      | One instance per container                 |
| Prototype      | A new instance is created on every request |
| Lazy Singleton | Created only when first is requested       |

Singletons are cached inside the ```ComponentFactory``` and prototype components are created on demand
and never cached.

<br>

## Lifecycle ##

The container supports standard lifecycle callbacks:
- ```@PostConstruct``` is executed after the component is created
- ```@PreDestroy``` is executed when ```Container.close()``` is called.

Only singleton components participate in lifecycle management.

<br>

## Usage ##

### Define components ###
```java
@Component
public class ServiceA {
    private final ServiceB serviceB;

    @Autowired
    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String getName() {
        return "ServiceA";
    }
    
    public ServiceB getServiceB(){
        return this.serviceB;
    }
}
```
```java
@Component
public class ServiceB {
    public String getName(){
        return "ServiceB";
    }
}
```

<br>

### Create a configuration class ###
```java
@Configuration
public class AppConfig { }
```

<br>

### Start the container and retrieve components ###
```java
Container container = new Container(AppConfig.class);
```
```java
ServiceA serviceA = container.getComponent(ServiceA.class);

serviceA.getName();        // ServiceA
serviceA.getServiceB().getName(); // ServiceB
```

<br>

### Interface inection ###

```java
import org.example.DIMechanism.Annotations.Primary;

public interface PaymentService {
    String pay();
}

@Component
public class PaymentService1 implements PaymentService {
    public String pay() {
        return "Payment service 1";
    }
}

@Component
@Primary
public class PaymentService2 implements PaymentService {
    public String pay() {
        return "Payment service 2";
    }
}
```
```java
@Component
public class CheckoutService {
    private final PaymentService paymentService;

    @Autowired
    public CheckoutService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String checkout() {
        return paymentService.pay();
    }
}
```

<br>

### Lazy and prototype ###
```java
@Component
@Lazy
public class HeavyService { }
```
```java
@Component
@Scope("prototype")
public class RequestContext { }
```

<br>

### Lifecycle ###
```java
@Component
public class CacheManager {

    @PostConstruct
    void init() {
        System.out.println("Cache initialized");
    }

    @PreDestroy
    void shutdown() {
        System.out.println("Cache destroyed");
    }
}
```