import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Basket implements Serializable {
    private String[] goods;
    private int[] prices;
    private int[] basketCount;
    int sum = 0;

    public Basket(String[] goods, int[] prices) {
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

    public void saveBin(File textFile) throws Exception {
        try
                (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(textFile))) {
            out.writeObject(this);
        }
    }

    public static Basket loadFromBinFile(File textFile) throws Exception {
        try
                (ObjectInputStream in = new ObjectInputStream(new FileInputStream(textFile))) {
            Basket basket = (Basket) in.readObject();
            return basket;
        }
    }
}



