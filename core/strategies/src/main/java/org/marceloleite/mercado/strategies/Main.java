package org.marceloleite.mercado.strategies;

import org.marceloleite.mercado.commons.CircularArray;

public class Main {
	
	public static void main(String[] args) {
		circularArrayList();
	}
	
	@SuppressWarnings("unused")
	private static void circularArrayList() {
		CircularArray<String> circularArrayList = new CircularArray<String>(4);
		circularArrayList.add("First");
		circularArrayList.add("Second");
		circularArrayList.add("Third");
		circularArrayList.add("Fourth");
		circularArrayList.add("Fifth");
		circularArrayList.add("Sixth");
		circularArrayList.add("Seventh");
		circularArrayList.add("Eighth");
		System.out.println(circularArrayList.get(0));
		System.out.println(circularArrayList.get(1));
		System.out.println(circularArrayList.get(2));
		System.out.println(circularArrayList.get(3));
	}
}
