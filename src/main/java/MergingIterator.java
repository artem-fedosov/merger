import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/*
MergingIterator is responsible for merging comparable iterators.
 */
class MergingIterator<T extends Comparable<T>, I extends Comparable<I> & Iterator<T>> implements Iterator<T> {

    private PriorityQueue<I> pq = new PriorityQueue<>();

    MergingIterator(List<I> iterators) {
        for (I iterator : iterators) {
            if (iterator.hasNext()) {
                pq.add(iterator);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return pq.size() > 0;
    }

    @Override
    public T next() {
        I iterator = pq.remove();
        T next = iterator.next();
        if (iterator.hasNext()) {
            pq.add(iterator);
        }
        return next;
    }
}