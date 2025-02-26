import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrinterFactory {
    private List<Map.Entry<Condition, Printer>> pairs = new ArrayList<>();

    public PrinterFactory() {
        Condition fizzBuzzCondition = number -> number % 15 == 0;
        Printer fizzBuzzPrinter = () -> "FizzBuzz";
        pairs.add(Map.entry(fizzBuzzCondition, fizzBuzzPrinter));
        Condition buzzCondition = number -> number % 5 == 0;
        Printer buzzPrinter = () -> "Buzz";
        pairs.add(Map.entry(buzzCondition, buzzPrinter));
        Condition fizzCondition = number -> number % 3 == 0;
        Printer fizzPrinter = () -> "Fizz";
        pairs.add(Map.entry(fizzCondition, fizzPrinter));
    }

    public String print(int number) {
        return pairs.stream()
                .filter(pair -> pair.getKey().isSatisfied(number))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(Printer::print)
                .orElse(String.valueOf(number));
    }
}