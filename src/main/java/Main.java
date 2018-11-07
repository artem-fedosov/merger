import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class Main {
    // Parse CLI arguments
    private static CommandLine parseArguments(String[] args) {
        OptionGroup sortOrderGroup = new OptionGroup();
        Option asc = new Option("a", "sort ascending");
        Option desc = new Option("d", "sort descending");
        sortOrderGroup.setRequired(false);
        sortOrderGroup.addOption(asc)
                .addOption(desc);

        OptionGroup itemTypeGroup = new OptionGroup();
        Option itemInt = new Option("i", "numeric sort");
        Option itemString = new Option("s", "string sort");
        itemTypeGroup.setRequired(true);
        itemTypeGroup.addOption(itemInt)
                .addOption(itemString);

        Options options = new Options();
        options.addOptionGroup(sortOrderGroup)
                .addOptionGroup(itemTypeGroup);

        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
            if (cmd.getArgs().length < 2) {
                throw new ParseException(
                        "Not enough arguments: output file name and at least one input file name is required");
            }
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("merger <OUTPUT_FILE> <INPUT_FILE>...", options);
            System.exit(1);
        }

        return cmd;
    }

    // Create merged output iterator, applying converter before ordering
    private static <R extends Comparable<R>> Iterator<String> getMerged(
            List<Iterator<String>> iterators,
            Function<String, R> converter,
            boolean reverse) {

        List<OrderingIterator<R>> ordered = new ArrayList<>();
        for (Iterator<String> iterator : iterators) {
            ordered.add(new OrderingIterator<>(
                    new ConvertingIterator<>(iterator, converter),
                    reverse
            ));
        }

        return new ConvertingIterator<>(
                new MergingIterator<>(ordered),
                Object::toString
        );
    }

    // Create list of iterators from a list of input files
    private static List<Iterator<String>> getFileIterators(List<String> fileNames) {
        List<Iterator<String>> iterators = new ArrayList<>();
        for (String filename : fileNames) {
            try {
                iterators.add(Files.lines(Paths.get(filename)).iterator());
            } catch (IOException e) {
                System.err.println("Unable to read file: " + filename);
            }
        }
        return iterators;
    }

    public static void main(String[] args) {
        CommandLine cmd = Main.parseArguments(args);
        List<String> fileNames = cmd.getArgList();
        String outputFileName = fileNames.get(0);
        List<String> inputFileNames = fileNames.subList(1, fileNames.size());

        boolean isIntegerSort = cmd.hasOption("i");
        boolean isReverseSort = cmd.hasOption("d");

        List<Iterator<String>> iterators = getFileIterators(inputFileNames);

        Iterator<String> result = isIntegerSort ?
                getMerged(iterators, Integer::parseInt, isReverseSort) :
                getMerged(iterators, x -> x, isReverseSort);

        Iterable<String> iterable = () -> result;
        try {
            Files.write(Paths.get(outputFileName), iterable);
        } catch (IOException e) {
            System.err.println("Unable to write to file: " + outputFileName);
        }

    }
}


