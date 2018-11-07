import java.util.Iterator;
import java.util.NoSuchElementException;

/*
OrderingIterator ensures values are ordered.
Out-of-order values from consumed iterator are discarded.
Sort order is controlled via `reverse` parameter.
 */
class OrderingIterator<T extends Comparable<T>> implements Iterator<T>, Comparable<OrderingIterator<T>> {
    private final Iterator<T> iterator;
    private final boolean reverse;
    private T next;

    OrderingIterator(Iterator<T> iterator, boolean reverse) {
        this.iterator = iterator;
        this.reverse = reverse;
        next = getNext(iterator, reverse);
    }

    private T getNext(Iterator<T> iterator, boolean reverse) {
        int reverser = reverse ? -1 : 1;
        while (iterator.hasNext()) {
            T newNext = iterator.next();
            if (next == null || reverser * next.compareTo(newNext) < 0) {
                return newNext;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public T next() {
        if (next == null) throw new NoSuchElementException();
        T oldNext = next;
        next = getNext(iterator, reverse);
        return oldNext;
    }

    // This class is used in MergingIterator's PriorityQueue.
    // This method implemented here, removes extra wrapper
    // in MergingIterator.
    @Override
    public int compareTo(OrderingIterator<T> o) {
        int multiplier = reverse ? -1 : 1;
        return next.compareTo(o.next) * multiplier;
    }
}