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
    private final Map<String, ComponentMetadata> componentsMetadata = new HashMap<>();
    private final Map<String, Object> components = new HashMap<>();
    private final Set<String> componentsInCreation = new HashSet<>();
    private final DependencyResolver dependencyResolver = new DependencyResolver(componentsMetadata);
    private final Class<?> configClass;
    private String packageName;

    public Container(Class<?> configClass){
        this.configClass = configClass;

        setPackageName();
        scanComponents();
        buildComponents();
    }

    private void scanComponents(){
        List<ComponentMetadata> componentsList = scanner.scan(packageName);
        for(ComponentMetadata component: componentsList) {
            componentsMetadata.put(component.getClassName(), component);
        }
    }

    private void buildComponents() {
        for(Map.Entry<String, ComponentMetadata> entry: componentsMetadata.entrySet()){
            String componentName = entry.getKey();
            getComponentInternal(componentName);
        }
    }

    public <T> T getComponent(Class<T> classType) {
        String componentName = dependencyResolver.resolve(classType);
        return classType.cast(getComponentInternal(componentName));
    }

    private Object getComponentInternal(String componentName){
        if(components.containsKey(componentName)){
            return components.get(componentName);
        }

        ComponentMetadata componentMetadata = componentsMetadata.get(componentName);
        if(componentMetadata == null) {
            throw new RuntimeException("No component metadata for " + componentName);
        }

        Object instance = createComponent(componentMetadata);
        components.put(componentName, instance);

        return instance;
    }

    private Object createComponent(ComponentMetadata componentMetadata) {
        Constructor<?> constructor = componentMetadata.getConstructor();
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];

        if(componentsInCreation.contains(componentMetadata.getClassName())){
            throw new RuntimeException("Circular dependencies in component " + componentMetadata.getClassName());
        }

        componentsInCreation.add(componentMetadata.getClassName());

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String dependencyName = dependencyResolver.resolve(parameter);
            args[i] = getComponentInternal(dependencyName);
        }

        componentsInCreation.remove(componentMetadata.getClassName());

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPackageName(){
        if(configClass.isAnnotationPresent(Configuration.class)){
            this.packageName = configClass.getPackageName();
        }
         else {
             throw new RuntimeException("No class with Configuration annotation was found");
         }
    }

    public Map<String, Object> getComponents() { return this.components; }

    public Map<String, ComponentMetadata> getComponentsMetadata(){
        return this.componentsMetadata;
    }

    public Set<String> getComponentsInCreation() {return this.componentsInCreation; }
}
