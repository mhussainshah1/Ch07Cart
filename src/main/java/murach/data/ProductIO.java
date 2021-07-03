package murach.data;

import murach.business.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ProductIO {

    public static Product getProduct(String code, String filepath) {
        try (var in = new BufferedReader(new FileReader(filepath))) {
            String line = in.readLine();
            while (line != null) {
                var t = new StringTokenizer(line, "|");
                String productCode = t.nextToken();
                if (code.equalsIgnoreCase(productCode)) {
                    String description = t.nextToken();
                    double price = Double.parseDouble(t.nextToken());
                    var product = new Product(code,description,price);
                    return product;
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public static List<Product> getProducts(String filepath) {
        List<Product> products = new ArrayList<>();
        try (var in = new BufferedReader(new FileReader(filepath))) {
            String line = in.readLine();
            while (line != null) {
                var t = new StringTokenizer(line, "|");
                var code = t.nextToken();
                var description = t.nextToken();
                var priceAsString = t.nextToken();
                var price = Double.parseDouble(priceAsString);
                Product product = new Product(code,description,price);
                products.add(product);
                line = in.readLine();
            }
            return products;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }
}