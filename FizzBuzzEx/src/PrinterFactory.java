import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PrinterFactory {
    private List<Map.Entry<Predicate<Integer>, Supplier<String>>> pairs = new ArrayList<>();

    public PrinterFactory() {
        pairs.add(Map.entry(number -> number % 15 == 0, () -> "FizzBuzz"));
        pairs.add(Map.entry(number -> number % 5 == 0, () -> "Buzz"));
        pairs.add(Map.entry(number -> number % 3 == 0, () -> "Fizz"));
    }

    public String print(int number) {
        return pairs.stream()
                .filter(pair -> pair.getKey().test(number))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(Supplier::get)
                .orElse(String.valueOf(number));
    }
}