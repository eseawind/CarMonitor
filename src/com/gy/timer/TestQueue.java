package com.gy.timer;

import java.util.HashMap;
import java.util.Queue;   
import java.util.LinkedList;   

import com.gy.Entity.TerminalInfoEntity;
public class TestQueue {   
    public static void main(String[] args) {   
        Queue<String> queue = new LinkedList<String>();   
//        queue.offer("Hello");   
//        queue.offer("World!");   
//        queue.offer("ÄãºÃ£¡");   
        System.out.println(queue.size());   
        queue.peek();
        String str;   
//        while((str=queue.poll())!=null){   
//            System.out.print(str);   
//        }   
        for (String x : queue) { 
//            System.out.println(x); 
    } 
//        System.out.println();   
//        System.out.println(queue.size());
        
        LinkedList<String> slink = new LinkedList<String>();
        slink.addFirst("a1");
        slink.addFirst("b2");
        slink.addFirst("c3");
//        System.err.println(slink);
        slink.addFirst("d4");
        slink.addFirst("e");
        slink.removeLast();
        System.err.println(slink);
        for (String s : slink ){
        	System.err.println(s);
        }
        HashMap<String, TerminalInfoEntity> listintersection = new  HashMap<String, TerminalInfoEntity>();
        TerminalInfoEntity t = new TerminalInfoEntity();
        TerminalInfoEntity t2 = new TerminalInfoEntity();
        listintersection.put("1", t);
        listintersection.put("2", t2);
        HashMap<String, TerminalInfoEntity> listintersection2 = new  HashMap<String, TerminalInfoEntity>();
        TerminalInfoEntity zt = new TerminalInfoEntity();
        TerminalInfoEntity zt2 = new TerminalInfoEntity();
        listintersection2.put("1", zt);
        listintersection2.put("3", zt2);  
        listintersection.remove(listintersection2 );
//        listintersection.remove("2");
        System.err.println(  listintersection2);
    }   
} 