package org.example.DIMechanism.Scanner;

import org.example.DIMechanism.Components.ComponentMetadata;

import java.util.List;

public interface Scanner {
    List<ComponentMetadata> scan(String packageName);
}
