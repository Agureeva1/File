
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

import java.util.*;


public class Main {
    protected static int[] saveOrNo = new int[2];
    protected static File forSave;
    protected static int logOrNo;
    protected static File forLog;
    protected static Basket basket = null;

    public static void main(String[] args) throws IOException {
        String testJS = new String();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("basket.json"));
            JSONObject jsonObject = (JSONObject) obj;
            testJS = jsonObject.toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        System.out.println(testJS);


        Scanner scanner = new Scanner(System.in);
        String[] goods = new String[0];
        goods = new String[]{"Хлеб", "Масло", "Молоко"};
        int[] prices = {50, 150, 100};
        int[] basketCount = new int[goods.length];

        File file = new File("basket.txt");


        File JsonFile = new File("basket.json");
        if (JsonFile.exists()) {
            basket = gson.fromJson(testJS, Basket.class);
        } else if (file.exists()) {
            basket = Basket.loadFromTxtFile(file);
        } else {
            basket = new Basket(goods, prices, basketCount);
        }
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
            ClientLog.log(productNum + 1, count);

        }
        // basket.saveTxt(file);

        basket.printCart();
        ClientLog.exportCSV(file);

        JSONObject obj = new JSONObject();

        obj.put("goods", goods);
        obj.put("basketCount", basketCount);
        obj.put("prices", prices);


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);

    }

    private static void read() {
        try {
            File fXmlFile = new File("shop.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList loadList = doc.getElementsByTagName("load");
            NodeList saveList = doc.getElementsByTagName("save");
            NodeList logList = doc.getElementsByTagName("log");
            Node loadNode = loadList.item(0);
            Node saveNode = saveList.item(0);
            Node logNode = logList.item(0);
            Element loadElement = (Element) loadNode;
            Element saveElement = (Element) saveNode;
            Element logElement = (Element) logNode;
            if (loadElement.getElementsByTagName("enabled").item(0).getTextContent().equals("false")) {
                System.out.println("Загрузка данных по прошлой сессии отключена в настройках.");
            } else if (loadElement.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                System.out.println("Корзина восстановлена");
                if (loadElement.getElementsByTagName("format").item(0).getTextContent().equals("json")) {
                    File jsonFile = new File(loadElement.getElementsByTagName("fileName").item(0).getTextContent());
                    basket = Basket.loadJSON(String.valueOf(jsonFile));
                } else if (loadElement.getElementsByTagName("format").item(0).getTextContent().equals("text")) {
                    File textFile = new File(loadElement.getElementsByTagName("fileName").item(0).getTextContent());
                    basket = Basket.loadFromTxtFile(textFile);
                }
            }


            if (saveElement.getElementsByTagName("enabled").item(0).getTextContent().equals("false")) {
                System.out.println("Сохранение корзины выключено");
                saveOrNo[0] = 0;
            } else if (saveElement.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                System.out.println("Сохранение корзины включено");
                saveOrNo[0] = 1;
                if (saveElement.getElementsByTagName("format").item(0).getTextContent().equals("json")) {
                    saveOrNo[1] = 0;
                    forSave = new File(saveElement.getElementsByTagName("fileName").item(0).getTextContent());
                } else if (saveElement.getElementsByTagName("format").item(0).getTextContent().equals("text")) {
                    saveOrNo[1] = 1;
                    forSave = new File(saveElement.getElementsByTagName("fileName").item(0).getTextContent());
                }
            }

            if (logElement.getElementsByTagName("enabled").item(0).getTextContent().equals("false")) {
                System.out.println("Сохранение логов выключено");
            } else if (logElement.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                System.out.println("Сохранение логов включено");
                logOrNo = 1;
                forLog = new File(logElement.getElementsByTagName("fileName").item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

