package com.gy.listener;

import java.util.HashMap;

public class testHashmap {
	public static void main(String[] args) {
		HashMap map = new HashMap<String, String>();
		map.put("key1", "asdf");
		System.err.println(map.get("key1"));
//		map.remove("key1");
		System.err.println(map.get("key1") );	
		System.err.println(map);
	}
}
