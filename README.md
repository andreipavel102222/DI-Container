# Java Dependency Injection Container

A lightweight Dependency Injection container inspired by Spring, built from scratch
using Java Reflection. This project explores the internal mechanics of DI frameworks such as Spring,
including component scanning, dependency resolution and lifecycle management.

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


## Design Principles

The container is built around a clean separation of responsibilities.

| Component           | Responsability                                                      |
|---------------------|---------------------------------------------------------------------|
| Container           | Public API. Entry point for creating and retrieving components.     |
| ComponentFactory    | Creates and caches component instances                              |
| ComponentMetadata   | Extracts reflection-based information (scope, lazy, lifecycle,etc.) |
| Dependency Resolver | Extracts constructor dependencies                                   |
| Scanner             | Discovers component classes in the classpath                        |


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


## Scopes

| Scope          | Behavior                                   |
|----------------|--------------------------------------------|
| Singleton      | One instance per container                 |
| Prototype      | A new instance is created on every request |
| Lazy Singleton | Created only when first is requested       |

Singletons are cached inside the ```ComponentFactory``` and prototype components are created on demand
and never cached.


## Lifecycle ##

The container supports standard lifecycle callbacks:
- ```@PostConstruct``` is executed after the component is created
- ```@PreDestroy``` is executed when ```Container.close()``` is called.

Only singleton components participate in lifecycle management.


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


### Create a configuration class ###
```java
// Defines the base package for component scanning.
@Configuration
public class AppConfig { }
```


### Start the container and retrieve components ###
```java
Container container = new Container(AppConfig.class);
```
```java
ServiceA serviceA = container.getComponent(ServiceA.class);

serviceA.getName();        // ServiceA
serviceA.getServiceB().getName(); // ServiceB
```


### Interface injection ###

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
```java
Container container = new Container(AppConfig.class);
// ...
container.close(); // triggers @PreDestroy on singleton beans
```


## Future Improvements ##
- **Thread-safe singleton creation**  
    - Currently, singleton components are not protected against concurrent access. In a multithreaded environment, this could lead to multiple instances being created.
  A future version could introduce synchronization or double-checked locking to ensure
  safe lazy initialization of singleton components.

- **Configuration via JSON**  
    - Allow defining components and dependencies without annotations, enabling externalized configuration.

- **Setter / Field Injection**
    - Support alternative injection strategies in addition to constructor injection.