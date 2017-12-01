import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    // We implement the randomized queue using a resizable array in order to avoid iterating across an arbitrary number of elements whenever popping items
    private Item[] items;
    private int n;
    
    public RandomizedQueue()
    {
        items = (Item[]) new Object[2];
        n = 0;
    }
    
    public boolean isEmpty()
    { return n == 0; }
    
    public int size()
    { return n; }
    
    private void resize(int capacity)
    {
        // The newly resized array should always be large enough to hold all of our elements
        assert (capacity >= n);
        
        Item[] temp = (Item[]) new Object[capacity];
        
        for (int i = 0; i < n; i++)
        {
            temp[i] = items[i];
        }
        
        items = temp;
    }
    
    public void enqueue(Item item)
    {
        // If we've hit max capacity, double the array's size
        if (n == items.length) { resize(2 * items.length); }
        
        items[n++] = item;
    }
    
    public Item dequeue()
    {
        if (isEmpty()) { throw new NoSuchElementException("Empty queue."); }
        
        int randomIndex;
        
        if (n > 1)
        { randomIndex = StdRandom.uniform(0, n-1); }
        else
        { randomIndex = 0; }
        
        // To avoid having holes in our array, we move the last element to the recently emptied random position upon each dequeue
        Item item = items[randomIndex];
        items[randomIndex] = items[n-1];
        items[n-1] = null;
        --n;
        
        // If our array is only a quarter full, halve its size
        if (n > 0 && n == items.length / 4) { resize(items.length / 2); }
        
        return item;
    }
    
    public Item sample()
    {
        if (isEmpty()) { throw new NoSuchElementException("Empty queue."); }
        
        int randomIndex;
        
        if (n > 1)
        { randomIndex = StdRandom.uniform(0, n); }
        else
        { randomIndex = 0; }
        
        return items[randomIndex];
    }
    
    public Iterator<Item> iterator()
    { return new RandomizedQueueIterator(); }
    
    // To iterate over the array in a normal manner while still providing random items, we shuffle it first
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int i;
        
        public RandomizedQueueIterator()
        {
            i = 0;
            StdRandom.shuffle(items, 0, n);
        }
        
        public boolean hasNext()
        { return i < n; }
        
        public void remove()
        { throw new UnsupportedOperationException(); }
        
        public Item next()
        {
            if (!hasNext()) { throw new NoSuchElementException(); }
            
            return items[i++];
        }
    }
    
    // Crude testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        
        int n = 100; // Number of items
        int k = 1000; // Number of sample iterations
        
        for (int i = 0; i < n; i++)
        {
            queue.enqueue(i);
        }
        
        int[][] table = new int[3][n];
        
        for (int i = 0; i < k; i++)
        {
            int val = queue.sample();
            table[0][val] += 1;
        }
        
        for (int val : queue)
        {
            table[1][val] += 1;
        }
        
        for (int i = 0; i < n; i++)
        {
            table[2][queue.dequeue()] += 1;
        }
        
        
        System.out.print("SAMPLE: ");
        for (int i = 0; i < n; i++)
        {
            System.out.print(table[0][i]);
            System.out.print(" ");
        }
        System.out.println();
        System.out.print("ITERATOR: ");
        for (int i = 0; i < n; i++)
        {
            System.out.print(table[1][i]);
            System.out.print(" ");
        }
        System.out.println();
        System.out.print("DEQUEUE: ");
        for (int i = 0; i < n; i++)
        {
            System.out.print(table[2][i]);
            System.out.print(" ");
        }
    }
}