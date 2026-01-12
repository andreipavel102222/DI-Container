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
    private boolean isClosed = false;

    public Container(Class<?> configClass){
        this.configClass = configClass;

        String packageName = setPackageName();
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

    private String setPackageName(){
        if(configClass.isAnnotationPresent(Configuration.class)){
            return configClass.getPackageName();
        }

        throw new RuntimeException("No class with Configuration annotation was found");
    }
}
