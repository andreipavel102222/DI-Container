package DIMechanism.Components;

import DIMechanism.Annotations.Autowired;
import DIMechanism.Annotations.Lazy;
import DIMechanism.Annotations.Primary;

import java.lang.reflect.Constructor;

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
                    throw new RuntimeException("Class " + getComponentName() + " has multiple autowired constructors");
                }
                autowiredConstructor = constructor;
            }
        }
        if(autowiredConstructor == null) {
            throw new RuntimeException("Class " + getComponentName() + " has no autowired constructor");
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

}
