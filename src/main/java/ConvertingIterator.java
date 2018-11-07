import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/*
ConvertingIterator is responsible for converting one type to another using `converter`.
Converting errors are communicated via returning null values.
Alternatively, exception can be raised if has to be propagated.
 */

public class ConvertingIterator<T, R> implements Iterator<R> {
    private final Iterator<T> iterator;
    private final Function<T, R> converter;
    private R next;

    ConvertingIterator(Iterator<T> iterator, Function<T, R> converter) {
        this.iterator = iterator;
        this.converter = converter;
        next = getNext(iterator, converter);
    }

    private R getNext(Iterator<T> iterator, Function<T, R> converter) {
        while (iterator.hasNext()) {
            T nextIn = iterator.next();
            R nextOut = converter.apply(nextIn);
            if (nextOut != null) {
                return nextOut;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public R next() {
        if (next == null) throw new NoSuchElementException();
        R oldNext = next;
        next = getNext(iterator, converter);
        return oldNext;
    }


}
