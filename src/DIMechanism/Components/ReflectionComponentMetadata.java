package DIMechanism.Components;

import DIMechanism.Annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionComponentMetadata implements ComponentMetadata {
    private final Class<?> componentClass;

    public ReflectionComponentMetadata(Class<?> componentClass){
        this.componentClass = componentClass;
    }

    @Override
    public String getComponentName() {
        return componentClass.getName();
    }

    @Override
    public Class<?> getClazz() {
        return componentClass;
    }

    @Override
    public String getScope() {
        if(componentClass.isAnnotationPresent(Scope.class)){
            Scope scope = componentClass.getAnnotation(Scope.class);
            if(!scope.value().equals("singleton") && !scope.value().equals("prototype")){
                throw new RuntimeException("Component " + getComponentName() + " has no valid scope");
            }
            return scope.value();
        }
        return "singleton";
    }

    @Override
    public Constructor<?> getConstructor() throws RuntimeException{
        Constructor<?>[] constructors = componentClass.getConstructors();
        Constructor<?> autowiredConstructor = null;
        if(constructors.length == 1) {
            return constructors[0];
        }
        for(Constructor<?> constructor: constructors) {
            if(constructor.isAnnotationPresent(Autowired.class)){
                if(autowiredConstructor != null) {
                    throw new RuntimeException("Component " + getComponentName() + " has multiple autowired constructors");
                }
                autowiredConstructor = constructor;
            }
        }
        if(autowiredConstructor == null) {
            throw new RuntimeException("Component " + getComponentName() + " has no autowired constructor");
        }
        return autowiredConstructor;
    }

    @Override
    public boolean isPrimary() {
        return componentClass.isAnnotationPresent(Primary.class);
    }

    @Override
    public boolean isLazy() {
        return componentClass.isAnnotationPresent(Lazy.class);
    }

    @Override
    public Method getPostConstruct() {
        Method postConstruct = null;

        for(Method method : componentClass.getMethods()){
            if(method.isAnnotationPresent(PostConstruct.class)) {
                if(postConstruct != null) {
                    throw new RuntimeException(getComponentName() + " has multiple @PostConstruct methods");
                }
                postConstruct = method;
            }
        }

        return postConstruct;
    }

    @Override
    public Method getPreDestroy() {
        Method preDestroy = null;

        for(Method method : componentClass.getMethods()){
            if(method.isAnnotationPresent(PreDestroy.class)) {
                if(preDestroy != null) {
                    throw new RuntimeException(getComponentName() + " has multiple @PreDestroy methods");
                }
                preDestroy = method;
            }
        }

        return preDestroy;
    }

}
