package adt;

//Nice file you got here

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CustomHashMap<K,V> implements Map<K,V> {
    
    private Tuple[] map;
    int size = 0;
    
    public int hashFunction(Object obj){
        int hash = 0;
        String str = obj.toString();
        char[] chars = str.toCharArray();
        for(int i = 0; i<chars.length;i++){
            if(i%2 == 0){
                hash = hash + chars[i];
            }    
            else
                hash = hash + (chars[i]*chars[i]);
        }
        hash = hash%map.length;
        return hash;
    }
    
    public CustomHashMap(){
        //Super high prime number
        map =  (Tuple[]) new Object[999983];
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0)
            return true;
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V put(K k, V v) {
        size++;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public V remove(Object o) {
        size--;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private abstract class ViewIterator<T> implements Iterator<T> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        protected Map.Entry<K,V> nextEntry() {
            return map[index++];
        }

        @Override
        public void remove() {
            map[--index] = new Tuple((K) ";",(V) "deleted tuple");
            //list.remove(--index);
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

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    class Tuple implements Map.Entry<K,V>{
        K key;
        V value;
        
        public Tuple(){
            key = null;
            value = null;
        }
        
        public Tuple(K k, V v){
            key = k;
            value = v;
        }
        
        @Override
        public K getKey() {
            return key;
        }
        
        public void remove(){
            key = (K) ";";
            value = (V) "deleted tuple";        
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
        
        public boolean equals(Tuple t){
            if(this.key.equals(t.key) && this.value.equals(t.value)){
                return true;
            }
            return false;
        }
    }
    
}
