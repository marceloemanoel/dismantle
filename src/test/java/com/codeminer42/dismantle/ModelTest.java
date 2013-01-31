package com.codeminer42.dismantle;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ModelTest {

    class ModelExample extends Model {

        public String birthdate;
        public Double distance;
        public String content;

        public ModelExample(Map<String, String> rep) {
            super(rep);
        }

        @Override
        protected Map<String, String> externalRepresentationKeyPaths() {
            Map<String, String> extRep = new HashMap<String, String>();
            extRep.put("birthdate", "birth_date");
            extRep.put("distance", "distance");
            extRep.put("content", "CONTENT");
            return extRep;
        }

        public Object transformToBirthdate(Object obj) {
            return obj;
        }

        public Object transformToDistance(Object obj) {
            try {
                return parseDouble((String) obj);
            } catch(Exception e) {
            }
            return null;
        }
    }

    @Test
    public void testPropertiesWithTransformMethods() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("birth_date", "2002");
        map.put("distance", "2000.0");
        ModelExample example = new ModelExample(map);
        assertThat(example.distance, is(2000.0));
        assertThat(example.birthdate, is("2002"));
    }

    @Test
    public void testPropertiesWithoutTransformMethods() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("CONTENT", "Codeminer 42");
        ModelExample example = new ModelExample(map);
        assertThat(example.content, is("Codeminer 42"));
        assertThat(example.distance, nullValue());
        assertThat(example.birthdate, nullValue());
    }
}
