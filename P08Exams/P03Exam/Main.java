package P08Exams.P03Exam;

import P08Exams.P03Exam.core.CategorizatorImpl;
import P08Exams.P03Exam.models.Category;

public class Main {
    public static void main(String[] args) {
        CategorizatorImpl categoryManager = new CategorizatorImpl();

        // Create categories
        Category electronics = new Category("electronics", "Electronics", "Electronic gadgets");
        Category mobiles = new Category("mobiles", "Mobiles", "Mobile phones");
        Category laptops = new Category("laptops", "Laptops", "Laptop computers");

        // Add categories to the manager
        categoryManager.assignParent("electronics", electronics.getName());
        categoryManager.assignParent("mobiles", mobiles.getName());
        categoryManager.assignParent("laptops", laptops.getName());


        // Assign parent
        categoryManager.assignParent("mobiles", "electronics");

        // Try assigning the same parent again (should throw an exception)
        try {
            categoryManager.assignParent("mobiles", "electronics");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}