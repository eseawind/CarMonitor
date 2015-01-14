package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.gy.Entity.TerminalInfoEntity;

public class TestLinkedList {
	LinkedList<Integer> l = new LinkedList<Integer>();
	private static List<Integer> getListIntersection(List<Integer> list1,List<Integer> list2){
//		List<String> listintersection = new  ArrayList<String>();
//		listintersection.addAll(list1);
//		listintersection.retainAll(list2);
		list1.removeAll(list2);
//		return listintersection;
		return list1;
	} 	
	public static void main(String[] args) {
		List<Integer> list1 = new  ArrayList<Integer>();
		List<Integer> list2 = new  ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		list2.add(1);
		list2.add(2);
		System.err.println(TestLinkedList.getListIntersection(list1, list2));
//		System.err.println(list1);
		
	}
}
