package main;

import dao.DatabaseConnector;
import dao.ProductDao;
import exceptions.ProductNotFoundException;
import model.Category;
import model.Product;
import prototype.CloneableProduct;
import utils.ProductQuantityComparator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        ProductDao productDao = new ProductDao(databaseConnector);

        try {
            productDao.addProduct(new Product("Phone", 699.99, 10, Category.ELECTRONICS));
            productDao.addProduct(new Product("Laptop", 999.99, 5, Category.ELECTRONICS));
            productDao.addProduct(new Product("Apple", 0.99, 100, Category.GROCERIES));

            productDao.deleteProduct("Phone");

            // wyswietlanie
            System.out.println("Products in database:");
            ArrayList<Product> products = productDao.getAllProducts();
            for (Product product : products) {
                System.out.println(product);
            }

            // sortowanie i wyswietlanie po sortowaniu
            Collections.sort(products, new ProductQuantityComparator());
            System.out.println("\nSorted Products by Quantity:");
            for (Product product : products) {
                System.out.println(product);
            }

            // klonowanie
            if (!products.isEmpty()) {
                CloneableProduct clonedProduct = products.get(0); // Klonowanie pierwszego produktu
                Product cloned = clonedProduct.clone();
                System.out.println("\nCloned Product: " + cloned);
                productDao.addProduct(cloned);

            } else {
                System.out.println("\nNo products available for cloning.");
            }




        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
