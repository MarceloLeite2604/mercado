package org.marceloleite.mercado.dao.cache.base;

public class CacheElement<T> {

	private boolean dirty;

	private T object;

	public CacheElement(T object) {
		this.object = object;
		this.dirty = false;
	}

	boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
		this.dirty = false;
	}
}
