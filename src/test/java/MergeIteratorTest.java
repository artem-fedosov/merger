import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MergeIteratorTest {
    static  <T extends Comparable<T>> List<T> getResults(boolean reverse, Iterator<T>... iters) {
        List<OrderingIterator<T>> orderedIterators = new ArrayList<>();
        for (Iterator<T> iter: iters) {
            orderedIterators.add(new OrderingIterator<>(iter, reverse));
        }

        Iterator<T> result = new MergingIterator<>(orderedIterators);
        List<T> list = new ArrayList<>();
        result.forEachRemaining(list::add);
        return list;
    }

    @Test
    public void mergeIteratorTest__multipleIterators__returnsMerged() {
        List<Integer> a = Arrays.asList(1, 3, 5);
        List<Integer> b = Arrays.asList(2, 4, 6);
        List<Integer> c = Arrays.asList(1, 4, 7);
        assertEquals(
                getResults(false, a.iterator(), b.iterator(), c.iterator()),
                Arrays.asList(1, 1, 2, 3, 4, 4, 5, 6, 7)
        );
    }

    @Test
    public void mergeIteratorTest__multipleIteratorsWithReverse__returnsReverseMerged() {
        List<Integer> a = Arrays.asList(5, 3, 1);
        List<Integer> b = Arrays.asList(6, 4, 2);
        List<Integer> c = Arrays.asList(7, 4, 1);
        assertEquals(
                getResults(true, a.iterator(), b.iterator(), c.iterator()),
                Arrays.asList(7, 6, 5, 4, 4, 3, 2, 1, 1)
        );
    }

    @Test
    public void mergeIteratorTest__oneIterator__returnsSame() {
        List<Integer> a = Arrays.asList(1, 3, 5);
        assertEquals(
                getResults(false, a.iterator()),
                Arrays.asList(1, 3, 5)
        );
    }

    @Test
    public void mergeIteratorTest__emptyIterator__returnsEmpty() {
        assertEquals(
                getResults(false),
                new ArrayList<Integer>()
        );
    }
}
