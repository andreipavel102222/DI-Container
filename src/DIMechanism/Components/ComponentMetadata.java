package DIMechanism.Components;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface ComponentMetadata {
    String getComponentName();
    Class<?> getClazz();
    String getScope();
    Constructor<?> getConstructor() throws RuntimeException;
    boolean isPrimary();
    boolean isLazy();
    Method getPostConstruct();
}
