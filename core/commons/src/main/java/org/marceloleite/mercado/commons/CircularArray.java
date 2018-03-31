package org.marceloleite.mercado.commons;

import java.util.ArrayList;
import java.util.List;

public class CircularArray<T extends Object> {

	private int arraySize;

	private int occupiedPositions;
	
	private int lastPosition;

	private int firstPosition;

	private Object[] buffer;
	
	// private int size;

	public CircularArray(int arraySize) {
		super();
		if (arraySize <= 0) {
			throw new IllegalArgumentException("Invalid circular array size: " + arraySize);
		}
		this.arraySize = arraySize;
		clear();
	}

	public void add(T object) {
		updatePositions();
		buffer[lastPosition] = object;
		updateSize();
	}

	private void updateSize() {
		if (occupiedPositions < arraySize) {
			occupiedPositions++;
		}
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		checkIndex(index);
		return (T) buffer[calculatePosition(index)];
	}

	private void checkIndex(int index) {
		if (index >= occupiedPositions) {
			throw new IllegalArgumentException("The array has " + occupiedPositions
					+ " position(s) occupied. It cannot get the element at position " + index + ".");
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

	public int getOccupiedPositions() {
		return occupiedPositions;
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
			if (occupiedPositions == arraySize) {
				firstPosition = addPosition(firstPosition, 1);
			}
		}
	}

	private int addPosition(int position, int valueToAdd) {
		int realPosition = position;
		realPosition += valueToAdd;
		realPosition %= arraySize;
		return realPosition;
	}

	private int calculatePosition(int position) {
		int realPosition = firstPosition;
		realPosition += position;
		realPosition %= arraySize;
		return realPosition;
	}

	@SuppressWarnings("unchecked")
	public List<T> asList() {
		List<T> list = new ArrayList<>(occupiedPositions);
		for (int counter = 0; counter < occupiedPositions; counter++) {
			list.add((T) buffer[calculatePosition(counter)]);
		}
		return list;
	}

	public void clear() {
		this.buffer = new Object[arraySize];
		this.occupiedPositions = 0;
		this.lastPosition = -1;
		this.firstPosition = -1;
	}

	public boolean isFilled() {
		return (occupiedPositions == arraySize);
	}
	
	public int getVacantPositions() {
		return (arraySize - occupiedPositions);
	}
}
