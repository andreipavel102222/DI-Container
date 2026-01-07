package DIMechanism.Components;

import java.lang.reflect.Constructor;

public interface ComponentMetadata {
    String getClassName();
    Class<?> getClazz();
    String getScope();
    Constructor<?> getConstructor() throws RuntimeException;
}
