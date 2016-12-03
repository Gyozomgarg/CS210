package adt;

import java.util.Map;
import java.util.AbstractMap;
import java.util.Set;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.AbstractCollection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ListMap<K,V> implements Map<K,V> {
	private List<Map.Entry<K,V>> list;
	
	public ListMap() {
		super();
		list = new ArrayList<Map.Entry<K,V>>();
	}
	
	public ListMap(Map<? extends K,? extends V> map) {
		this();
		this.putAll(map);
	}
	
	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		for (Map.Entry<K,V> e: list) {
			if (key == null ? e.getKey() == null : key.equals(e.getKey()))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for (Map.Entry<K,V> e: list) {
			if (value == null ? e.getValue() == null : value.equals(e.getValue()))
				return true;
		}
		return false;
	}

	@Override
	public V get(Object key) {
		for (Map.Entry<K,V> e: list) {
			if (key == null ? e.getKey() == null : key.equals(e.getKey()))
				return e.getValue();
		}
		return null;
	}
	
	@Override
	public V put(K key, V value) {
		for (Map.Entry<K,V> e: list) {
			if (key == null ? e.getKey() == null : key.equals(e.getKey())) {
				V before = e.getValue();
				e.setValue(value);
				return before;
			}
		}
		// TODO: implement entry as inner class
		Map.Entry<K,V> make = new AbstractMap.SimpleEntry<K,V>(key, value);
		list.add(make);
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> e: map.entrySet()) {
			this.put(e.getKey(), e.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		Iterator<Map.Entry<K,V>> iter = list.iterator();
		while (iter.hasNext()) {
			Map.Entry<K,V> e = iter.next();
			if (key == null ? e.getKey() == null : key.equals(e.getKey())) {
				V before = e.getValue();
				iter.remove();
				return before;
			}
		}
		return null;
	}
	
	private abstract class ViewIterator<T> implements Iterator<T> {
		int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < list.size();
		}
		
		protected Map.Entry<K,V> nextEntry() {
			return list.get(index++);
		}

		@Override
		public void remove() {
			list.remove(--index);
		}
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
				return ListMap.this.size();
			}
			
			@Override
			public void clear() {
				ListMap.this.clear();
			}
		};
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
				return ListMap.this.size();
			}
			
			@Override
			public void clear() {
				ListMap.this.clear();
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
				return ListMap.this.size();
			}
			
			@Override
			public void clear() {
				ListMap.this.clear();
			}
		};
	}
	
	// TODO: correctly implement equals method

	// TODO: correctly implement hashCode method
	
	// TODO: correctly implement toString method
}
