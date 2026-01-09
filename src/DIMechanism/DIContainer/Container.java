package DIMechanism.DIContainer;

import DIMechanism.Annotations.Configuration;
import DIMechanism.Annotations.Qualifier;
import DIMechanism.Components.ComponentMetadata;
import DIMechanism.Scanner.*;
import DIMechanism.Scanner.Scanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class Container {
    private final Scanner scanner = new ReflectionScanner();
    private final ComponentFactory componentFactory = new ComponentFactory();
    private final Class<?> configClass;
    private String packageName;

    public Container(Class<?> configClass){
        this.configClass = configClass;

        setPackageName();

        Map<String, ComponentMetadata> componentsMap = scanComponents();
        componentFactory.addComponentsMetadata(componentsMap);
        componentFactory.buildSingletonComponents();
    }

    private Map<String, ComponentMetadata> scanComponents(){
        List<ComponentMetadata> componentsList = scanner.scan(packageName);
        Map<String, ComponentMetadata> componentsMap = new HashMap<>();
        for(ComponentMetadata component: componentsList) {
            componentsMap.put(component.getComponentName(), component);
        }
        return componentsMap;
    }

    public <T> T getComponent(Class<T> classType) {
        return classType.cast(componentFactory.getComponentInternal(classType));
    }

    private void setPackageName(){
        if(configClass.isAnnotationPresent(Configuration.class)){
            this.packageName = configClass.getPackageName();
        }
         else {
             throw new RuntimeException("No class with Configuration annotation was found");
         }
    }

    public Map<String, Object> getComponents() { return this.componentFactory.getComponents(); }

    public Map<String, ComponentMetadata> getComponentsMetadata() { return this.componentFactory.getComponentsMetadata(); }

    public Set<String> getComponentsInCreation() { return this.componentFactory.getComponentsInCreation(); }
}
