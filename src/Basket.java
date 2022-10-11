import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Basket {
    private String[] goods;
    private int[] prices;
    private int[] basketCount;
    int sum = 0;

    public Basket(String[] goods, int[] prices, int[] basketCount) {
        this.goods = goods;
        this.prices = prices;
        this.basketCount = new int[goods.length];
    }

    public void addToCart(int productNum, int amount) {
        basketCount[productNum] += amount;
    }

    public void printCart() {
        for (int i = 0; i < basketCount.length; i++) {
            if (basketCount[i] != 0) {
                System.out.println(goods[i] + " - " + basketCount[i] + " шт." + " по " + prices[i] + " руб/шт. ");
            }
            sum += basketCount[i] * prices[i];
        }

        System.out.println("Итого: " + sum + " руб. ");
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String product : goods) {
                out.print(product + " ");

            }
            out.println();
            for (int price : prices) {
                out.print(price + " ");
            }
            out.println();
            for (int count : basketCount) {
                out.print(count + " ");
            }
            out.println();
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        try (Scanner scanner = new Scanner(new FileInputStream(textFile))) {
            String[] goods = scanner.nextLine().split(" ");
            int[] prices = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            int[] basket = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            return new Basket(goods, prices, basket);
        }
    }

}
