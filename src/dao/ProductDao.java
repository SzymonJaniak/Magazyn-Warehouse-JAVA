package dao;

import model.Product;
import model.Category;
import exceptions.ProductNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductDao {
    private final DatabaseConnector databaseConnector;
    private final HashMap<String, Product> productMap;

    public ProductDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        this.productMap = new HashMap<>();
        createDatabaseIfNotExists();
        createProductsTable();
    }

    private void createDatabaseIfNotExists() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS warehouse");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createProductsTable() {
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "price DECIMAL(10, 2) NOT NULL," +
                    "quantity INT NOT NULL," +
                    "category VARCHAR(50) NOT NULL" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, price, quantity, category) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getCategory().name());
            preparedStatement.executeUpdate();
            productMap.put(product.getName(), product);
        }
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement statement = databaseConnector.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String categoryString = resultSet.getString("category");

                Category category;
                try {
                    category = Category.valueOf(categoryString);
                } catch (IllegalArgumentException e) {
                    category = Category.GROCERIES;
                    System.out.println("Unknown category: " + categoryString + ". Defaulting to " + category);
                }

                Product product = new Product(name, price, quantity, category);
                products.add(product);
                productMap.put(name, product);
            }
        }
        return products;
    }

    public void deleteProduct(String name) throws SQLException, ProductNotFoundException {
        if (!productMap.containsKey(name)) {
            throw new ProductNotFoundException("Product not found: " + name);
        }

        String sql = "DELETE FROM products WHERE name = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            productMap.remove(name);
        }
    }

    public void updateProduct(Product product) throws SQLException, ProductNotFoundException {
        if (!productMap.containsKey(product.getName())) {
            throw new ProductNotFoundException("Product not found: " + product.getName());
        }

        String sql = "UPDATE products SET price = ?, quantity = ?, category = ? WHERE name = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, product.getPrice());
            preparedStatement.setInt(2, product.getQuantity());
            preparedStatement.setString(3, product.getCategory().name());
            preparedStatement.setString(4, product.getName());
            preparedStatement.executeUpdate();
            productMap.put(product.getName(), product);
        }
    }

    public Product getProductByName(String name) throws ProductNotFoundException {
        if (!productMap.containsKey(name)) {
            throw new ProductNotFoundException("Product not found: " + name);
        }
        return productMap.get(name);
    }
}
