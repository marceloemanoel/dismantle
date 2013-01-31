package com.codeminer42.dismantle;

import java.util.Map;

public abstract class Model {
    abstract protected Map<String, String> externalRepresentationKeyPaths();

    protected Model(Map<String, String> externalRepresentation) {
        for (String property : externalRepresentation.keySet()) {
            String transformation = externalRepresentation.get(property);
        }
    }
}
