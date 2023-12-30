package P08Exams.P03Exam.core;

import P08Exams.P03Exam.models.Category;

public interface Categorizator {
    void addCategory(Category category);

    void assignParent(String childCategoryId, String parentCategoryId);

    void removeCategory(String categoryId);

    boolean contains(Category category);

    int size();

    Iterable<Category> getChildren(String categoryId);

    Iterable<Category> getHierarchy(String categoryId);

    Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName();
}
