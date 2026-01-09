package DIMechanism.DIContainer;

import DIMechanism.Components.ComponentMetadata;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

class ComponentFactory {
    private final Map<String, ComponentMetadata> componentsMetadata = new HashMap<>();
    private final Map<String, Object> components = new HashMap<>();
    private final Set<String> componentsInCreation = new HashSet<>();
    private final DependencyResolver dependencyResolver = new DependencyResolver(componentsMetadata);

    protected void buildSingletonComponents() {
        for(Map.Entry<String, ComponentMetadata> entry: componentsMetadata.entrySet()){
            String componentName = entry.getKey();
            ComponentMetadata componentMetadata = entry.getValue();
            if(componentMetadata.getScope().equals("singleton") && !componentMetadata.isLazy()) {
                getComponentInternal(componentName);
            }
        }
    }

    protected Object getComponentInternal(Class<?> classType){
        String componentName = dependencyResolver.resolve(classType);
        return getComponentInternal(componentName);
    }

    private Object getComponentInternal(String componentName){
        ComponentMetadata componentMetadata = componentsMetadata.get(componentName);
        if(componentMetadata == null) {
            throw new RuntimeException("No component metadata for " + componentName);
        }

        if(componentMetadata.getScope().equals("singleton") && components.containsKey(componentName)){
            return components.get(componentName);
        }

        Object instance = createComponent(componentMetadata);

        if(componentMetadata.getScope().equals("singleton")){
            components.put(componentName, instance);
        }

        return instance;
    }

    private Object createComponent(ComponentMetadata componentMetadata) {
        Constructor<?> constructor = componentMetadata.getConstructor();
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];

        if(componentsInCreation.contains(componentMetadata.getComponentName())){
            throw new RuntimeException("Circular dependencies in component " + componentMetadata.getComponentName());
        }

        componentsInCreation.add(componentMetadata.getComponentName());

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String dependencyName = dependencyResolver.resolve(parameter);
            args[i] = getComponentInternal(dependencyName);
        }

        componentsInCreation.remove(componentMetadata.getComponentName());

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addComponentsMetadata(List<ComponentMetadata> scannedComponents){
        for(ComponentMetadata component: scannedComponents) {
            componentsMetadata.put(component.getComponentName(), component);
        }
    }

    public Map<String, Object> getComponents() { return this.components; }

    public Map<String, ComponentMetadata> getComponentsMetadata(){
        return this.componentsMetadata;
    }

    public Set<String> getComponentsInCreation() {return this.componentsInCreation; }
}
