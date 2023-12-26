package P07Augmentation;

import java.util.*;

public class oldShoppingCentre {
    private Set<Product> products;
    private Map<String, SortedSet<Product>> productsByProducer;
    private Map<String, SortedSet<Product>> productsByName;
    private TreeMap<Double, LinkedHashSet<Product>> productsByPrice;

    public oldShoppingCentre() {
        this.products = new HashSet<>();
        this.productsByProducer = new HashMap<>();
        this.productsByName = new HashMap<>();
        this.productsByPrice = new TreeMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);

        if (this.products.add(product)) {
            this.productsByProducer.putIfAbsent(producer, new TreeSet<>(Comparator.comparingDouble(Product::getPrice)));
            this.productsByProducer.get(producer).add(product);

            this.productsByName.putIfAbsent(name, new TreeSet<>(Comparator.comparingDouble(Product::getPrice)));
            this.productsByName.get(name).add(product);

            this.productsByPrice.putIfAbsent(price, new LinkedHashSet<Product>());
            this.productsByPrice.get(price).add(product);

            return "P07Augmentation.Product added" + System.lineSeparator();
        } else {
            return "";
        }
    }

    public String delete(String name, String producer) {
        Set<Product> removeByName = this.productsByName.remove(name);
        Set<Product> removeByProducer = this.productsByProducer.remove(producer);

        int size = this.products.size();

        if (removeByName != null && removeByProducer != null) {
            this.products.removeAll(removeByName);
            this.products.removeAll(removeByProducer);

            Set<Product> productsToRemove = new HashSet<>(removeByName);
            productsToRemove.addAll(removeByProducer);
            removeFromProductByPrice(productsToRemove);
        }

        size = size - this.products.size();

        if (size > 0) {
            return size + " products deleted" + System.lineSeparator();
        } else {
            return "No products found" + System.lineSeparator();
        }
    }

    public String delete(String producer) {
        Set<Product> productsToRemove = this.productsByProducer.remove(producer);
        int size = productsToRemove.size();

        if (size > 0) {
            this.products.removeAll(productsToRemove);

            for (Product product : productsToRemove) {
                Set<Product> productsByNameToRemove = this.productsByName.get(product.getName());
                productsByNameToRemove.removeAll(productsToRemove);
            }

            removeFromProductByPrice(productsToRemove);

            return size + " products deleted" + System.lineSeparator();
        }
        return "No products found" + System.lineSeparator();
    }

    public String findProductsByName(String name) {
        Set<Product> productsByNameSet = this.productsByName.get(name);
        return generateProductsList(productsByNameSet);
    }

    public String findProductsByProducer(String producer) {
        Set<Product> productsByProducerSet = this.productsByProducer.get(producer);
        return generateProductsList(productsByProducerSet);
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        NavigableMap<Double, LinkedHashSet<Product>> doubleSortedSetSortedMap = this.productsByPrice
                .subMap(priceFrom, true, priceTo, true);

        StringBuilder sb = new StringBuilder();

        if (doubleSortedSetSortedMap != null) {
            for (LinkedHashSet<Product> set : doubleSortedSetSortedMap.values()) {
                sb.append(generateProductsList(set));
            }
        } else {
            sb.append("No products found").append(System.lineSeparator());
        }
        return sb.toString();
    }

    private String generateProductsList(Set<Product> set) {
        StringBuilder sb = new StringBuilder();

        if (set != null) {
            for (Product product : set) {
                sb.append(product.toString()).append(System.lineSeparator());
            }
        } else {
            sb.append("No products found").append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    private void removeFromProductByPrice(Set<Product> productsToRemove) {
        for (Product product : productsToRemove) {
            this.productsByPrice.get(product.getPrice()).remove(product);
        }
    }
}