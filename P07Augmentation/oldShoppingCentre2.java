package P07Augmentation;

import java.util.*;
import java.util.stream.Collectors;

public class oldShoppingCentre2 {
    private Set<Product> products;
    private Map<String, HashSet<Product>> productsByProducer;

    public oldShoppingCentre2() {
        this.products = new HashSet<>();
        this.productsByProducer = new HashMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);

        if (this.products.add(product)) {
            this.productsByProducer.putIfAbsent(producer, new HashSet<>());
            this.productsByProducer.get(producer).add(product);

            return "P07Augmentation.Product added" + System.lineSeparator();
        } else {
            return "";
        }
    }

    public String delete(String name, String producer) {
        int size = this.products.size();
        int newSIze = 0;

        if (this.productsByProducer.containsKey(producer)) {
            List<Product> removeByProducer = this.productsByProducer.remove(producer)
                    .stream()
                    .filter(product -> product.getName().equals(name))
                    .collect(Collectors.toList());

            if (!removeByProducer.isEmpty()) {
                this.products.removeAll(removeByProducer);
            }

            newSIze = size - this.products.size();
        }

        if (newSIze > 0) {
            return newSIze + " products deleted" + System.lineSeparator();
        } else {
            return "No products found" + System.lineSeparator();
        }
    }

    public String delete(String producer) {

        HashSet<Product> removeByProducer = this.productsByProducer.remove(producer);
        int size = removeByProducer.size();

        if (size > 0) {
            this.products.removeAll(removeByProducer);
            return size + " products deleted" + System.lineSeparator();
        }
        return "No products found" + System.lineSeparator();
    }

    public String findProductsByName(String name) {
        StringBuilder sb = new StringBuilder();

        if (!this.products.isEmpty()) {
            this.products
                    .stream()
                    .filter(product -> product.getName().equals(name))
                    .sorted(Comparator.comparing(Product::getName)
                            .thenComparing(Product::getProducer)
                            .thenComparing(Product::getPrice))
                    .forEach(product -> {
                        sb.append(product.toString()).append(System.lineSeparator());
                    });
        }

        String toReturn = sb.toString();

        return getStringToReturn(toReturn);
    }

    public String findProductsByProducer(String producer) {
        StringBuilder sb = new StringBuilder();

        if (this.productsByProducer.containsKey(producer)) {
            this.productsByProducer.get(producer)
                    .stream()
                    .sorted(Comparator.comparing(Product::getName)
                            .thenComparing(Product::getProducer)
                            .thenComparing(Product::getPrice))
                    .forEach(product -> {
                        sb.append(product.toString()).append(System.lineSeparator());
                    });

        }
        String toReturn = sb.toString();

        return getStringToReturn(toReturn);
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        StringBuilder sb = new StringBuilder();

        if (!this.products.isEmpty()) {
            this.products.stream()
                    .filter(p -> p.getPrice() >= priceFrom && p.getPrice() <= priceTo)
                    .sorted(Comparator.comparing(Product::getName)
                            .thenComparing(Product::getProducer)
                            .thenComparing(Product::getPrice))
                    .forEach(product -> {
                        sb.append(product.toString()).append(System.lineSeparator());
                    });
        }
        String toReturn = sb.toString();

        return getStringToReturn(toReturn);
    }

    private String getStringToReturn(String toReturn) {
        if (toReturn.length() > 0) {
            return toReturn;
        } else {
            return "No products found" + System.lineSeparator();
        }
    }
}
