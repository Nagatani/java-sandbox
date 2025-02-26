
/**
 * FizzBuzzメインプログラム
 */
public class Main {
    public static void main(String[] args) {
        PrinterFactory factory = new PrinterFactory();

        java.util.stream.IntStream.rangeClosed(1, 100)
                  .mapToObj(factory::print)
                  .forEach(System.out::println);
    }
}