package DIMechanism.DIContainer;

import DIMechanism.Annotations.Qualifier;
import DIMechanism.Components.ComponentMetadata;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public class DependencyResolver {
    private final Map<String, ComponentMetadata> componentsMetadata;

    public DependencyResolver (Map<String, ComponentMetadata> componentsMetadata){
        this.componentsMetadata = componentsMetadata;
    }

    public String resolve(Parameter parameter){
        String qualifier = resolveQualifier(parameter);
        return resolveComponentsName(parameter.getType(), qualifier);
    }

    public String resolve(Class<?> classType){
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

        if(components.size() > 1) {
            throw new RuntimeException("Multiple components for the type " + type.getName());
        }

        return components.get(0);
    }
}
