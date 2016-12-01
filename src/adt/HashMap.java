package adt;

import java.io.Serializable;
import java.util.Map;

/** 
 * This is currently just an alias for a built-in
 * implementation of HashMap and it is therefore
 * noncompliant with the Final Module specification.
 * 
 * However, it will temporarily satisfy the needs of all
 * other modules until an original HashMap is implemented.
 */
public class HashMap<K,V> extends java.util.HashMap<K,V> implements Map<K,V>, Serializable{
	public HashMap() {
		super();
	}
	
	public HashMap(Map<? extends K, ? extends V> copy) {
		super(copy);
	}
}
