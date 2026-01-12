package org.example.DIMechanism.Container;

import org.example.DIMechanism.Annotations.Configuration;
import org.example.DIMechanism.Components.ComponentMetadata;
import org.example.DIMechanism.Scanner.ReflectionScanner;
import org.example.DIMechanism.Scanner.Scanner;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Container {
    private final ComponentFactory componentFactory = new ComponentFactory();
    private final Class<?> configClass;
    private String packageName;
    private boolean isClosed = false;

    public Container(Class<?> configClass){
        this.configClass = configClass;

        setPackageName();

        Scanner scanner = new ReflectionScanner();
        List<ComponentMetadata> componentsList = scanner.scan(packageName);
        componentFactory.addComponentsMetadata(componentsList);
        componentFactory.buildSingletonComponents();
    }

    public <T> T getComponent(Class<T> classType) {
        if(isClosed){
            throw new RuntimeException("Container is closed");
        }
        return classType.cast(componentFactory.getComponentInternal(classType));
    }

    public void close(){
        componentFactory.close();
        isClosed = true;
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
