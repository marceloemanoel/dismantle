package com.codeminer42.dismantle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class Model {
    abstract protected Map<String, String> externalRepresentationKeyPaths();

    protected Model(Map<String, String> externalRepresentation) {
        Map<String, String> selfRepresentation = this.externalRepresentationKeyPaths();
        for (String property : selfRepresentation.keySet()) {
            String transformation = externalRepresentation.get(selfRepresentation.get(property));
            assignProperty(property, transformation);
        }
    }

    private void assignProperty(String property, String mapValue) {
        Object result = null;
        try {
            try {
                result = tryToInvokeTransformation(property, mapValue);
            } catch (NoSuchMethodException e) {
                // try to setTheField with the result we got
                result = mapValue;
            } catch(Exception e) {
                // Unexpected error while trying to invoke the transform method.
            } finally {
                this.tryToSetField(property, result);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private Object tryToInvokeTransformation(String property, String mapValue) throws NoSuchMethodException {
        try {
            Method method = this.getClass().getMethod("transformTo" + property.substring(0, 1).toUpperCase() + property.substring(1), Object.class);
            return method.invoke(this, mapValue);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e ) {
            e.printStackTrace();
        }
        return null;
    }

    private void tryToSetField(String property, Object data) {
        try {
            Field field = this.getClass().getDeclaredField(property);
            field.setAccessible(true);
            field.set(this, data);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
