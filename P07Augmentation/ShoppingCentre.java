package P07Augmentation;

import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCentre {
    private Map<String, List<Product>> productsByProducer;

    public ShoppingCentre() {
        this.productsByProducer = new HashMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        this.productsByProducer.putIfAbsent(producer, new ArrayList<>());
        this.productsByProducer.get(producer).add(product);
        return "P07Augmentation.Product added" + System.lineSeparator();

    }

    public String delete(String producer) {

        int size = 0;
        if (this.productsByProducer.containsKey(producer)) {
            List<Product> deletedProducts = this.productsByProducer.get(producer);
            if (!deletedProducts.isEmpty()) {
                size = deletedProducts.size();
                this.productsByProducer.get(producer).removeAll(deletedProducts);
            }

        }

        if (size > 0) {
            return size + " products deleted" + System.lineSeparator();
        } else {
            return "No products found" + System.lineSeparator();
        }
    }

    public String delete(String name, String producer) {
        int size = 0;
        if (this.productsByProducer.containsKey(producer)) {
            List<Product> removeByProducer = this.productsByProducer.get(producer)
                    .stream()
                    .filter(product -> product.getName().equals(name))
                    .collect(Collectors.toList());
            if (!removeByProducer.isEmpty()) {
                size = removeByProducer.size();
                this.productsByProducer.get(producer).removeAll(removeByProducer);
            }
        }

        if (size > 0) {
            return size + " products deleted" + System.lineSeparator();
        } else {
            return "No products found" + System.lineSeparator();
        }
    }


    public String findProductsByName(String name) {
//        StringBuilder sb = new StringBuilder();
//        if (!this.productsByProducer.isEmpty()) {
//            for (List<P07Augmentation.Product> list : productsByProducer.values()) {
//                list.stream()
//                        .filter(product -> product.getName().equals(name))
//                        .sorted(Comparator.comparing(P07Augmentation.Product::getName)
//                                .thenComparing(P07Augmentation.Product::getProducer)
//                                .thenComparing(P07Augmentation.Product::getPrice))
//                        .forEach(product -> {
//                            sb.append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())).append(System.lineSeparator());
//                        });
//            }
//        }
//        return sb.length() > 0 ? sb.toString() : "No products found" + System.lineSeparator();



        StringBuilder sb = new StringBuilder();

        this.productsByProducer.values().stream()
                .flatMap(Collection::stream)
                .filter(product -> product.getName().equals(name))
                .sorted(Comparator.comparing(Product::getName)
                        .thenComparing(Product::getProducer)
                        .thenComparing(Product::getPrice))
                .forEach(product -> sb.append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())).append(System.lineSeparator()));

        return sb.length() > 0 ? sb.toString() : "No products found" + System.lineSeparator();
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
                        sb.append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())).append(System.lineSeparator());
                    });

        }


        return sb.length() > 0 ? sb.toString() : "No products found" + System.lineSeparator();
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        StringBuilder sb = new StringBuilder();
        this.productsByProducer.values().stream()
                .flatMap(Collection::stream)
                .filter(product -> product.getPrice() >= priceFrom && product.getPrice() <= priceTo)
                .sorted(Comparator.comparing(Product::getName)
                        .thenComparing(Product::getProducer)
                        .thenComparing(Product::getPrice))
                .forEach(product -> sb.append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())).append(System.lineSeparator()));

        return sb.length() > 0 ? sb.toString() : "No products found" + System.lineSeparator();
    }
}