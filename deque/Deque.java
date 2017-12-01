import java.util.Iterator;
import java.util.NoSuchElementException;

// The Java equivalent of C++ templates is the concept of generics, in which a class is followed by the generic <Item>
public class Deque<Item> implements Iterable<Item>
{
    // Using a linked-list implementation to avoid the rather more complicated process of resizing an array from both directions
    private Node<Item> first, last;
    private int size;
    
    // Each node should hold the location of adjacent nodes to facilitate pushing from either direction
    private class Node<Item>
    {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }
    
    // Initialize the deque with null first and last nodes
    public Deque()
    { 
        first = null;
        last = null;
        size = 0;
    }
    
    public boolean isEmpty()
    { return size == 0; }
    
    public int size()
    { return size; }
    
    public void addFirst(Item item)
    {
        if (item == null) { throw new IllegalArgumentException(); }
        
        Node<Item> newFirst = new Node<Item>();
        
        newFirst.item = item;
        newFirst.next = first;
        newFirst.prev = null;
        
        // If this is the first item of the list, it is simultaneously the last and the first entry
        if (isEmpty())
        { last = newFirst; }
        // Otherwise, the old first node now has its prev pointer set to the new first node
        else
        { first.prev = newFirst; }
        
        first = newFirst;
        
        ++size;
    }
    
    public void addLast(Item item)
    {
        if (item == null) { throw new IllegalArgumentException(); }
        
        Node<Item> newLast = new Node<Item>();
        
        newLast.item = item;
        newLast.next = null;
        newLast.prev = last;
        
        if (isEmpty())
        { first = newLast; }
        else
        { last.next = newLast; }
        
        last = newLast;
        
        ++size;
    }
    
    public Item removeFirst()
    {
        if (isEmpty()) { throw new NoSuchElementException("Deque is empty."); }
        
        Item item = first.item;
        first = first.next;
        
        // This is in a different location than our addFirst and addLast functions so that we can properly execute the following if statement
        --size;
        
        if (isEmpty())
        { last = null; }
        else
        { first.prev = null; }
        
        return item;
    }
    
    public Item removeLast()
    {
        if (isEmpty()) { throw new NoSuchElementException("Deque is empty."); }
        
        Item item = last.item;
        last = last.prev;
        
        --size;
        
        if (isEmpty())
        { last = null; }
        else
        { last.next = null; }
        
        return item;
    }
    
    // An iterator function implements the iterable 'template' class
    public Iterator<Item> iterator()
    { return new DequeIterator<Item>(first); }
    
    // Our DequeIterator function implements an iterable's iterator
    private class DequeIterator<Item> implements Iterator<Item>
    {
        private Node<Item> current;
        
        DequeIterator(Node<Item> first)
        { current = first; }
        
        public boolean hasNext()
        { return current != null; }
        
        public void remove()
        { throw new UnsupportedOperationException(); }
        
        public Item next()
        {
            if (!hasNext()) { throw new NoSuchElementException(); }
            
            Item item = current.item;
            current = current.next;
            
            return item;
        }
    }
    
    // Crude but effective testing
    public static void main(String[] args) {
        Deque<Integer> test = new Deque<Integer>();
        
        System.out.println("addFirst(1)");
        test.addFirst(1);
        
        System.out.println("addFirst(2)");
        test.addFirst(2);
        
        System.out.println("removeLast()");
        System.out.println(test.removeLast());
        
        System.out.println("addLast(3)");
        test.addLast(3);
        
        System.out.println("addFirst(4)");
        test.addFirst(4);
        
        System.out.println("addLast(5)");
        test.addLast(5);
        
        System.out.println("removeFirst()");
        System.out.println(test.removeFirst());
        
        System.out.println("addLast(6)");
        test.addLast(6);
        
        System.out.println("removeFirst()");
        System.out.println(test.removeFirst());
        
        System.out.println("removeLast()");
        System.out.println(test.removeLast());
        
        System.out.println("removeLast()");
        System.out.println(test.removeLast());
        
        System.out.println("removeFirst()");
        System.out.println(test.removeFirst());
    }
}