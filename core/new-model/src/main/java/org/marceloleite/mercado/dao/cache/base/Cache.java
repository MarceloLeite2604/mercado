package org.marceloleite.mercado.dao.cache.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Cache<K, T> {
	
	private Map<K, CacheElement<T>> map;
	
	public T get(K key) {
		T result = null;
		CacheElement<T> cacheElement = getCacheElement(key);
		if ( cacheElement != null && !cacheElement.isDirty()) {
			result = cacheElement.getObject();
		}
		return result;
	}
	
	public void setDirty(K key) {
		CacheElement<T> cacheElement = getCacheElement(key);
		if ( cacheElement != null ) {
			cacheElement.setDirty(true);
		}
	}
	
	private CacheElement<T> getCacheElement(K key) {
		return getMap().get(key);
	}
	
	private Map<K, CacheElement<T>> getMap() {
		if ( map == null ) {
			map = new HashMap<>();
		}
		return map;
	}
	
	public T put(K key, T element) {
		getMap().put(key, new CacheElement<T>(element));
		return element;
	}
	
	
}
