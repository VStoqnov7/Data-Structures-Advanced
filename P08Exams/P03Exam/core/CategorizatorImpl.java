package P08Exams.P03Exam.core;

import P08Exams.P03Exam.models.Category;

import java.util.*;
import java.util.stream.Collectors;

public class CategorizatorImpl implements Categorizator {

    private Map<String, Category> categories;
    private Map<String, Set<String>> parentChildMap;

    public CategorizatorImpl(){
        this.categories = new HashMap<>();
        this.parentChildMap = new HashMap<>();
    }
    @Override
    public void addCategory(Category category) {
        if (categories.containsKey(category.getId())) {
            throw new IllegalArgumentException("Category already exists.");
        }
        categories.put(category.getId(), category);
    }
    @Override
    public void assignParent(String childCategoryId, String parentCategoryId) {
        if (!categories.containsKey(childCategoryId) || !categories.containsKey(parentCategoryId)) {
            throw new IllegalArgumentException("Either child or parent category does not exist.");
        }
        if (parentChildMap.getOrDefault(parentCategoryId, Collections.emptySet()).contains(childCategoryId)) {
            throw new IllegalArgumentException("Child category is already a child of the parent category.");
        }
        parentChildMap.computeIfAbsent(parentCategoryId, k -> new HashSet<>()).add(childCategoryId);
    }

    @Override
    public void removeCategory(String categoryId) {
        if (!categories.containsKey(categoryId)) {
            throw new IllegalArgumentException("Category does not exist.");
        }
        List<String> toRemove = new ArrayList<>();
        toRemove.add(categoryId);
        Queue<String> queue = new LinkedList<>();
        queue.add(categoryId);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            categories.remove(current);
            Set<String> children = parentChildMap.getOrDefault(current, Collections.emptySet());
            for (String childId : children) {
                toRemove.add(childId);
                queue.add(childId);
            }
            parentChildMap.remove(current);
            for (Set<String> childSet : parentChildMap.values()) {
                childSet.remove(current);
            }
        }
        for (String idToRemove : toRemove) {
            categories.remove(idToRemove);
        }
    }

    @Override
    public boolean contains(Category category) {
        return categories.containsValue(category);
    }

    @Override
    public int size() {
        return categories.size();
    }

    @Override
    public Iterable<Category> getChildren(String categoryId) {
        if (!categories.containsKey(categoryId)) {
            throw new IllegalArgumentException("Category does not exist.");
        }

        Set<Category> children = new HashSet<>();
        collectChildren(categoryId, children);
        return children;
    }

    private void collectChildren(String categoryId, Set<Category> children) {
        Set<String> directChildren = parentChildMap.getOrDefault(categoryId, Collections.emptySet());
        for (String childId : directChildren) {
            Category child = categories.get(childId);
            children.add(child);
            collectChildren(childId, children);
        }
    }

    @Override
    public Iterable<Category> getHierarchy(String categoryId) {
        if (!categories.containsKey(categoryId)) {
            throw new IllegalArgumentException("Category does not exist.");
        }

        List<Category> hierarchy = new ArrayList<>();
        collectHierarchy(categoryId, hierarchy);
        Collections.reverse(hierarchy);
        return hierarchy;
    }

    private void collectHierarchy(String categoryId, List<Category> hierarchy) {
        if (categories.containsKey(categoryId)) {
            Category category = categories.get(categoryId);
            hierarchy.add(category);
            Set<String> parentIds = parentChildMap.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(categoryId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            for (String parentId : parentIds) {
                collectHierarchy(parentId, hierarchy);
            }
        }
    }

    @Override
    public Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName() {
        List<Category> sortedCategories = new ArrayList<>(categories.values());

        sortedCategories.sort((c1, c2) -> {
            int depth1 = getDepthOfChildren(c1.getId());
            int depth2 = getDepthOfChildren(c2.getId());

            if (depth1 != depth2) {
                return Integer.compare(depth2, depth1);
            } else {
                return c1.getName().compareTo(c2.getName());
            }
        });

        return sortedCategories.subList(0, Math.min(3, sortedCategories.size()));
    }

    private int getDepthOfChildren(String categoryId) {
        Set<String> children = parentChildMap.getOrDefault(categoryId, Collections.emptySet());
        int maxDepth = 0;
        for (String childId : children) {
            int childDepth = getDepthOfChildren(childId);
            maxDepth = Math.max(maxDepth, childDepth);
        }
        return maxDepth + 1;
    }
}