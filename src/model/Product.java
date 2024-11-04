package model;

import prototype.CloneableProduct;

public class Product implements CloneableProduct {
    private final String name;
    private final double price;
    private final int quantity;
    private final Category category;

    public Product(String name, double price, int quantity, Category category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public Product clone() {
        return new Product(this.name, this.price, this.quantity, this.category);
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + ", quantity=" + quantity + ", category=" + category + '}';
    }
}
