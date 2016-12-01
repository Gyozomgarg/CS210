package adt;

import java.io.Serializable;

import adt.HashMap;

/** 
 * This class is a hash map alias providing
 * a Field Name -> Field Value mapping.
 * 
 * Additional non-map features can be implemented.
 * Schema requirements are defined in the project.
 */
public class Row extends HashMap<String, Object> implements Serializable {
    public Row() {
    	super();
    }

    public Row(Row row) {
    	super(row);
    }

}