import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class OrderedIteratorTest {
    private static <T extends Comparable<T>> List<T> getResult(List<T> input, boolean reverse) {
        OrderingIterator<T> iter = new OrderingIterator<>(input.iterator(), reverse);
        List<T> result = new ArrayList<>();
        iter.forEachRemaining(result::add);
        return result;
    }
    @Test
    public void orderedInput__returnsOrdered() {
        List<Integer> input = Arrays.asList(1, 3, 5);
        assertEquals(
                getResult(input, false),
                input
        );
    }

    @Test
    public void reverseOrderedInput__returnsOrdered() {
        List<Integer> input = Arrays.asList(5, 3, 1);
        assertEquals(
                getResult(input, true),
                input
        );
    }

    @Test
    public void mixedInput__skipsOutOfOrderValues() {
        List<Integer> input = Arrays.asList(1, 5, 3, 8, 4, 10);
        assertEquals(
                getResult(input, false),
                Arrays.asList(1, 5, 8, 10)
        );
    }

    @Test
    public void reverseMixedInput__skipsOutOfOrderValues() {
        List<Integer> input = Arrays.asList(10, 4, 8, 3, 5, 1);
        assertEquals(
                getResult(input, true),
                Arrays.asList(10, 4, 3, 1)
        );
    }


    @Test
    public void emptyInput__emptyIterator() {
        List<Integer> input = new ArrayList<>();
        assertEquals(
                getResult(input, false),
                new ArrayList<>()
        );
    }
}