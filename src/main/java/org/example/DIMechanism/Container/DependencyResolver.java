package org.example.DIMechanism.Container;

import org.example.DIMechanism.Annotations.Qualifier;
import org.example.DIMechanism.Components.ComponentMetadata;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

class DependencyResolver {
    private final Map<String, ComponentMetadata> componentsMetadata;

    DependencyResolver (Map<String, ComponentMetadata> componentsMetadata){
        this.componentsMetadata = componentsMetadata;
    }

    protected String resolve(Parameter parameter){
        String qualifier = resolveQualifier(parameter);
        return resolveComponentsName(parameter.getType(), qualifier);
    }

    protected String resolve(Class<?> classType){
        return resolveComponentsName(classType, "");
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
                .map(ComponentMetadata::getComponentName)
                .toList();

        if(components.isEmpty()){
            throw new RuntimeException("No component was found for " + type.getName());
        }

        if(components.size() == 1){
            return components.get(0);
        }

        List<String> primaryComponents = components.stream()
                .filter(componentName -> componentsMetadata.get(componentName).isPrimary())
                .toList();

        if(primaryComponents.size() == 1) {
            return primaryComponents.get(0);
        }

        if(primaryComponents.size() > 1) {
            throw new RuntimeException("Multiple @Primary components found for the type " + type.getName());
        }

        throw new RuntimeException("Multiple components for the type " + type.getName() +
                " and none marked as @Primary");
    }
}
