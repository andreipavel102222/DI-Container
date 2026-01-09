package DIMechanism.DIContainer;

import DIMechanism.Annotations.Configuration;
import DIMechanism.Components.ComponentMetadata;
import DIMechanism.Scanner.ReflectionScanner;
import DIMechanism.Scanner.Scanner;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Container {
    private final ComponentFactory componentFactory = new ComponentFactory();
    private final Class<?> configClass;
    private String packageName;

    public Container(Class<?> configClass){
        this.configClass = configClass;

        setPackageName();

        Scanner scanner = new ReflectionScanner();
        List<ComponentMetadata> componentsList = scanner.scan(packageName);
        componentFactory.addComponentsMetadata(componentsList);
        componentFactory.buildSingletonComponents();
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
