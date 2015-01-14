package test;


import java.util.LinkedList;

/**
 * 用LinkedList实现栈
 * 
 * 队列和栈区别：队列先进先出，栈先进后出。
 * 
 * @author 林计钦
 * @version 1.0 Sep 5, 2013 11:24:34 PM
 */
public class Stack<T> {
     LinkedList<T> storage = new LinkedList<T>();
    
    
    /** 入栈 */
    public void push(T v) {
        storage.addFirst(v);
        
    }

    /** 出栈，但不删除 */
    public T peek() {
        return storage.getFirst();
    }

    /** 出栈，删除 */
    public T pop() {
        return storage.removeFirst();
    }

    /** 栈是否为空 */
    public boolean empty() {
        return storage.isEmpty();
    }

    /** 打印栈元素 */
    public String toString() {
        return storage.toString();
    }
    
    public static void main(String[] args) {
        Stack stack=new Stack<String>();
        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.push("d");
        stack.push("e");
        for(Object s :  stack.storage ){
        	System.err.println(s);
        }
        System.err.println("---------"+stack.peek());
        //[c, b, a]
        System.out.println(stack.toString());
        //c--[c, b, a]
        Object obj=stack.peek();
        System.out.println(obj+"--"+stack.toString());
        obj=stack.pop();
        //c--[b, a]
        System.out.println(obj+"--"+stack.toString());
        //false
        System.out.println(stack.empty());
    }
}