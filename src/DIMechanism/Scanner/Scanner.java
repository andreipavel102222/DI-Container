package DIMechanism.Scanner;

import DIMechanism.Components.ComponentMetadata;

import java.util.List;

public interface Scanner {
    List<ComponentMetadata> scan(String packageName);
}
