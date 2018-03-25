package org.marceloleite.mercado.commons;

import java.util.ArrayList;
import java.util.List;

public class CircularArray<T extends Object> {

	private int bufferSize;

	private int totalOfValues;

	private int lastPosition;

	private int firstPosition;

	private Object[] buffer;
	
	private int size;

	public CircularArray(int size) {
		super();
		if (size <= 0) {
			throw new IllegalArgumentException("Invalid circular buffer size: " + size);
		}
		this.size = size;
		clear();
	}

	public void add(T object) {
		updatePositions();
		buffer[lastPosition] = object;
		updateSize();
	}

	private void updateSize() {
		if (totalOfValues < bufferSize) {
			totalOfValues++;
		}
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		checkIndex(index);
		return (T) buffer[calculatePosition(index)];
	}

	private void checkIndex(int index) {
		if (index >= totalOfValues) {
			throw new IllegalArgumentException("The array contains " + totalOfValues
					+ " element(s). It cannot get the element at position " + index + ".");
		}
	}

	public T first() {
		if (firstPosition == -1) {
			return null;
		} else {
			return get(firstPosition);
		}
	}

	public T last() {
		if (lastPosition == -1) {
			return null;
		} else {
			return get(lastPosition);
		}
	}

	public int getSize() {
		return totalOfValues;
	}

	private void updatePositions() {
		updateLastPosition();
		updateFirstPosition();
	}

	private void updateLastPosition() {
		if (lastPosition == -1) {
			lastPosition = 0;
		} else {
			lastPosition = addPosition(lastPosition, 1);
		}
	}

	private void updateFirstPosition() {
		if (firstPosition == -1) {
			firstPosition = 0;
		} else {
			if (totalOfValues == bufferSize) {
				firstPosition = addPosition(firstPosition, 1);
			}
		}
	}

	private int addPosition(int position, int valueToAdd) {
		int realPosition = position;
		realPosition += valueToAdd;
		realPosition %= bufferSize;
		return realPosition;
	}

	private int calculatePosition(int position) {
		int realPosition = firstPosition;
		realPosition += position;
		realPosition %= bufferSize;
		return realPosition;
	}

	@SuppressWarnings("unchecked")
	public List<T> asList() {
		List<T> list = new ArrayList<>();
		for (int counter = 0; counter < totalOfValues; counter++) {
			list.add((T) buffer[calculatePosition(counter)]);
		}
		return list;
	}

	public void clear() {
		this.buffer = new Object[size];
		this.bufferSize = size;
		this.totalOfValues = 0;
		this.lastPosition = -1;
		this.firstPosition = -1;
	}
}
