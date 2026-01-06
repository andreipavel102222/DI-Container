package com.DIMechanism.DIContainer;

import com.DIMechanism.Annotations.Configuration;
import com.DIMechanism.Annotations.Qualifier;
import com.DIMechanism.Components.ComponentMetadata;
import com.DIMechanism.Scanner.*;
import com.DIMechanism.Scanner.Scanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class Container {
    private final Scanner scanner = new ReflectionScanner();
    private final Map<String, ComponentMetadata> componentsMetadata = new HashMap<>();
    private final Map<String, Object> components = new HashMap<>();
    private final Set<String> componentsInCreation = new HashSet<>();
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
            ComponentMetadata componentMetadata = entry.getValue();

            if(!components.containsKey(componentName)){
                getComponentInternal(componentName, componentMetadata);
            }
        }
    }

    public <T> T getComponent(Class<T> classType) {
        String componentName = resolveComponentsName(classType, "");
        ComponentMetadata componentMetadata = componentsMetadata.get(componentName);
        return classType.cast(getComponentInternal(componentName, componentMetadata));
    }

    private Object getComponentInternal(String componentName, ComponentMetadata componentMetadata){
        if(components.containsKey(componentName)){
            return components.get(componentName);
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
            throw new RuntimeException("Circular dependecies in component " + componentMetadata.getClassName());
        }
        componentsInCreation.add(componentMetadata.getClassName());

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String qualifier = resolveQualifier(parameter);
            String dependencyName = resolveComponentsName(parameter.getType(), qualifier);
            if(!components.containsKey(dependencyName)){
                ComponentMetadata dependencyComponentMetadata = componentsMetadata.get(dependencyName);
                Object dependency = getComponentInternal(dependencyName, dependencyComponentMetadata);
                args[i] = dependency;
            }
            else {
                args[i] = components.get(dependencyName);
            }
        }

        componentsInCreation.remove(componentMetadata.getClassName());

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveQualifier(Parameter parameter) {
        if(parameter.isAnnotationPresent(Qualifier.class)){
            Qualifier qualifier = parameter.getAnnotation(Qualifier.class);
            if(!componentsMetadata.containsKey(qualifier.value())){
                throw new RuntimeException("No component found with qualifier " + qualifier);
            }
            return qualifier.value();
        }
        return "";
    }

    private String resolveComponentsName(Class<?> type, String qualifier){
        if(!qualifier.isEmpty()){
            return qualifier;
        }

        if(componentsMetadata.containsKey(type.getName())){
            return type.getName();
        }

        List<String> components = componentsMetadata.values().stream()
                .filter(componentMetadata -> type.isAssignableFrom(componentMetadata.getClazz()))
                .map(ComponentMetadata::getClassName)
                .toList();

        if(components.isEmpty()){
            throw new RuntimeException("No component was found for " + type.getName());
        }

        if(components.size() > 1) {
            throw new RuntimeException("Multiple components for the type " + type.getName());
        }

        return components.get(0);
    }

    private void setPackageName(){
        if(configClass.isAnnotationPresent(Configuration.class)){
            this.packageName = configClass.getPackageName();
        }
         else {
             throw new RuntimeException("No class with Configuration annotation was found");
         }
    }

    public Map<String, Object> getComponents() {
        return this.components;
    }

    public Map<String, ComponentMetadata> getComponentsMetadata(){
        return this.componentsMetadata;
    }
}
