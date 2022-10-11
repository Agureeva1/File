import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("basket.bin");
        Scanner scanner = new Scanner(System.in);
        String[] goods = {"Хлеб", "Масло", "Молоко"};
        int[] prices = {50, 150, 100};

        Basket basket = new Basket(goods, prices);


        System.out.println("Список товаров: ");
        for (int i = 0; i < goods.length; i++) {
            System.out.println((i + 1) + ". " + goods[i] + " " + prices[i] + " руб/шт.");
        }
        int sumProducts = 0;
        int productNum = 0;
        int count = 0;

        while (true) {
            System.out.println("Выберите товар и его количество или введите 'end'");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            productNum = Integer.parseInt(parts[0]) - 1;
            count = Integer.parseInt(parts[1]);
            int sum = count * prices[productNum];
            sumProducts += sum;
            basket.addToCart(productNum, count);

        }


        try {
            basket.saveBin(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        basket.printCart();

        try {
            basket = Basket.loadFromBinFile(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

