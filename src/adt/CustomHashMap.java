package adt;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class CustomHashMap<K,V> implements Map<K,V> {
    
    public Tuple<K,V>[] map;
    int size = 62503;
    int elements = 0;
    
    public int hashFunction(Object obj){
        int hash = 0;
        if(obj instanceof String){        
            String str = obj.toString();
            char[] chars = str.toCharArray();
            for(int i = 0; i<chars.length;i++){
                if(i%2 == 0){
                    hash = hash + chars[i];
                }    
                else
                    hash = hash + (chars[i]*chars[i]);
            }
        }
        else{
            hash = obj.hashCode();
        }
        hash = hash%map.length;
        return hash;
    }
    
    public CustomHashMap(){
        //Super high prime number
        map =  new Tuple[size];
    }
    
	public CustomHashMap(Map<? extends K, ? extends V> p){
        Object[] keySet= p.keySet().toArray();
        map =  new Tuple[999983];
        for(int i=0; i<p.keySet().size(); i++){
            this.put((K) keySet[i], (V) p.get(keySet[i]));
        }
    }
    
    @Override
    public int size() {
        return elements;
    }

    @Override
    public boolean isEmpty() {
        if(elements == 0)
            return true;
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        int hash = hashFunction(o);
        boolean found = false;
        Tuple<K,V> pointer = map[hash];
        while(found = false){
            try{
                pointer = map[hash];
            }
            catch(NullPointerException e){
                return found;
            }
            if(map[hash].getKey().equals((K) o)){
                found = true;
                return found;
            }
            else{
                try{
                    pointer = pointer.getNext();
                    continue;
                }
                catch(NullPointerException e){
                    return found;
                }
            }
        } 
        return found;
    }

    @Override
    public boolean containsValue(Object o) {
        boolean found = false;
        Tuple<K,V> pointer = null;
        try{
            for(int i = 0; i<map.length; i++){
                pointer = map[i];
                while(true)
                    try{
                        if(o.equals(pointer.getValue()))
                            return true;
                        else
                            pointer = pointer.getNext();
                        if(pointer == null)
                            break;
                    }
                    catch(NullPointerException e){
                        break;
                    }
            }
        }
        catch(ArrayIndexOutOfBoundsException a){
            return false;
        }
        return false;
    }

    @Override
    public V get(Object k) {
        int hash = hashFunction(k);
        V value = null;
        boolean found = false;
        Tuple<K,V> pointer = map[hash];
        Tuple<K,V> prevPoint = null;
        if(pointer == null){
        	return value;
        }
        while(found == false){
        	if(pointer.getKey().equals((K) k)){
        		value = pointer.getValue();
        		found = true;
        	}
        	else{
        		prevPoint = pointer;
        		if(pointer.getNext() != null)
        			pointer = pointer.getNext();
        		else{
        			break;
        		}
        	}
        		
        }
        
        return value;
    }
    
    public Tuple<K,V> getTuple(Object k) {
        int hash = hashFunction(k);
        Tuple<K,V> value = null;
        boolean found = false;
        Tuple<K,V> pointer = map[hash];
        Tuple<K,V> prevPoint = null;
        if(pointer == null)
            return value;
        while(found == false){
            if(pointer.getKey().equals((K) k)){
                value = pointer;
                found = true;
            }
            else{
                prevPoint = pointer;
                if(pointer.hasNext())
                        pointer = pointer.getNext();
                else{
                    break;
                }
            }       		
        }
        
        return value;
    }

    @Override
    public V put(K k, V v) {
        V oldVal = null;
        int hash = hashFunction(k);
        boolean next;
        next = true;
        Tuple<K,V> pointer = map[hash];
        Tuple<K,V> prevPoint = null;
	if(pointer == null){
            map[hash] = new Tuple<K,V>(k,v);
            elements++;
            return oldVal;
        }
        while(next){
            if(pointer == null){
                    next = false;	
                    pointer = new Tuple<K,V>(k,v);
                    if(prevPoint != null)
                        prevPoint.setNextTuple(pointer);
                    break;
            }
            if(pointer.getKey().equals(k)){
                oldVal = pointer.setValue(v);
                break;
            }
            else{
                prevPoint = pointer;
                pointer = pointer.getNext();
            }
        }    
        return oldVal;
    }

    @Override
    public V remove(Object o) {
    	K obj = (K) o;
        V oldVal = null;
        int hash = hashFunction((K) obj);
        boolean found = false;
        Tuple<K,V> pointer = map[hash];
        Tuple<K,V> prevPoint = null;
        while(found == false){
            if(pointer == null)
                return oldVal;       
            if(pointer.getKey().equals((K)obj)){
                oldVal = pointer.getValue();
                if(prevPoint == null){
                    map[hash] = pointer.getNext();
                    elements--;
                    break;
                }
                if(prevPoint != null){
                    prevPoint.setNextTuple(pointer.getNext());
                    elements--;
                    break;
                }
            }
            else{
                prevPoint = pointer;
                pointer = pointer.getNext();
            }
        }
        return oldVal;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {       
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        
        for(int i=0; i<keys.length; i++){
            K tempK = (K) keys[i];
            V tempV = (V) values[i];
            this.put(tempK, tempV);
        }
    }

    @Override
    public void clear() {
        map = (Tuple[]) new Object[999983];
    }

    private abstract class ViewIterator<T> implements Iterator<T> {
        int index = findFirst();
        Tuple<K,V> pointer = map[index];
        Tuple<K,V> prevPoint = pointer;
        
        
        @Override
        public boolean hasNext() {
            if(index == -1 && findFirst() == -1){
                return false;
            }
            if(pointer == null)
                return false;
            int tempHash = hashFunction(pointer.getKey());
            if(pointer.hasNext() == false && tempHash >= map.length-1)
                return false;
            return true;
        }

        protected Map.Entry<K,V> nextEntry() {
            if(index == -1){
                index = findFirst();
                pointer = map[index];
                return pointer;
            }
            if(pointer.hasNext()){
                if(prevPoint == null){
                    K tempKey = pointer.getKey();
                    prevPoint = map[hashFunction(tempKey)];
                    pointer = pointer.getNext();
                    return pointer;  
                }
                else{
                    pointer = pointer.getNext();
                    prevPoint = prevPoint.getNext();
                    return pointer;
                }
            }
            else{
                if(index+1 == map.length)
                    index = 0;
                else
                    index ++;
//                prevPoint = prevPoint.getNext();
                while(true){
                    try{    
                        if(index >= map.length)
                            throw new NoSuchElementException();
                        pointer = map[index];
                        if(pointer == null)
                            index++;
                        else{
    //                        prevPoint.setNextTuple(pointer);
                            return pointer;
                        }
                    }    
                    catch(NoSuchElementException e){
                        return map[findFirst()];
                    }
                }
            }
        }

        @Override
        public void remove() {
            prevPoint.setNextTuple(pointer.getNext());
            pointer = pointer.getNext();
        }

        private int findFirst() {
            int firstIndex = -1;
            for(int i=0; i<map.length; i++){
                if(map[i] != null){
                    firstIndex = i;
                    break;
                }
            }
            return firstIndex;
        }
    }
 
    private Set<K> keySetView = null;

	@Override
	public Set<K> keySet() {
            if (keySetView != null) return keySetView;
            else return keySetView = new AbstractSet<K>() {
                @Override
                public Iterator<K> iterator() {
                    return new ViewIterator<K>() {
                        @Override
                        public K next() {
                            return nextEntry().getKey();
                        }
                    };
                }

                @Override
                public int size() {
                    return CustomHashMap.this.size();
                }

                @Override
                public void clear() {
                    CustomHashMap.this.clear();
                }
            };
	}

    private Collection<V> valuesView = null;
        
    @Override
    public Collection<V> values() {
        if (valuesView != null) return valuesView;
            else return valuesView = new AbstractCollection<V>() {
                @Override
                public Iterator<V> iterator() {
                    return new ViewIterator<V>() {
                        @Override
                        public V next() {
                            return nextEntry().getValue();
                        }
                    };
                }

                @Override
                public int size() {
                    return map.length;
                }

                @Override
                public void clear() {
                    CustomHashMap.this.clear();
                }
            };
    }

    private Set<Map.Entry<K, V>> entrySetView = null;
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySetView != null) return entrySetView;
            else return entrySetView = new AbstractSet<Map.Entry<K,V>>() {
                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return new ViewIterator<Map.Entry<K, V>>() {
                        @Override
                        public Map.Entry<K, V> next() {
                            return nextEntry();
                        }
                    };
                }

                @Override
                public int size() {
                    return CustomHashMap.this.elements;
                }

                @Override
                public void clear() {
                    CustomHashMap.this.clear();
                }
            };
        
    }
    
    public class Tuple<K,V> implements Map.Entry<K,V>{
        K key;
        V value;
        public Tuple<K,V> next;
        
        public Tuple(){
            key = null;
            value = null;
        }
        
        public Tuple(K k, V v){
            key = k;
            value = v;
            next = null;
        }

        private Tuple(Tuple<K,V> tuple) {
            this.key = tuple.getKey();
            this.value = tuple.getValue();
            this.next = tuple.getNext();
        }
        
        public void setNextTuple(Tuple<K,V> t){
        	next = t;
        }
        
        public Tuple<K,V> getNext(){
        	return next;
        }
        
        public boolean hasNext(){
            if(next != null)
                    return true;
            return false;
        }
        
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }
        
        public boolean equals(Map.Entry<K,V> t){
            if(t == null){
                return this == null;
            }
            return this.key.equals(t.getKey()) && this.value.equals(t.getValue());
        }
        
        @Override
        public String toString(){
        	String str = this.key + "=" + this.value;
        	return str;
        }
    }
    
}