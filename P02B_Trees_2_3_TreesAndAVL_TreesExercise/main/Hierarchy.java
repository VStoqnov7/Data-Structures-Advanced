package P02B_Trees_2_3_TreesAndAVL_TreesExercise.main;

import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {
    private Map<T, HierarchyNode<T>> data;
    private HierarchyNode<T> root;

    public Hierarchy(T element) {
        this.data = new HashMap<>();

        HierarchyNode<T> root = new HierarchyNode<>(element);
        this.root = root;
        this.data.put(element, root);
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public void add(T element, T child) {
        HierarchyNode<T> parent = ensureExistAndGet(element);

        if(this.data.containsKey(child)){
            throw new IllegalArgumentException();
        }

        HierarchyNode<T> toBeAdded = new HierarchyNode<>(child);
        toBeAdded.setParent(parent);
        parent.getChildren().add(toBeAdded);

        this.data.put(child, toBeAdded);
    }

    @Override
    public void remove(T element) {
        HierarchyNode<T> toRemove = ensureExistAndGet(element);

        if(toRemove.getParent() == null){
            throw new IllegalStateException();
        }

        HierarchyNode<T> parent = toRemove.getParent();
        List<HierarchyNode<T>> children = toRemove.getChildren();

        children.forEach(n -> n.setParent(parent));

        parent.getChildren().addAll(children);
        parent.getChildren().remove(toRemove);

        this.data.remove(toRemove.getValue());
    }

    @Override
    public Iterable<T> getChildren(T element) {
        HierarchyNode<T> current = ensureExistAndGet(element);

        return current.getChildren().stream().map(n -> n.getValue()).collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        HierarchyNode<T> current = ensureExistAndGet(element);

        return current.getParent() == null ? null : current.getParent().getValue();
    }

    @Override
    public boolean contains(T element) {
        return this.data.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        List<T> result = new ArrayList<>();

        this.data.keySet().forEach(k -> {
            if(other.contains(k)){
                result.add(k);
            }
        });

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Deque<HierarchyNode<T>> deque = new ArrayDeque<>(
//                    Arrays.asList(root);
                    Collections.singletonList(root)
            );

            @Override
            public boolean hasNext() {
                return deque.size() > 0;
            }

            @Override
            public T next() {
                HierarchyNode<T> nextElement = deque.poll();
                deque.addAll(nextElement.getChildren());

                return nextElement.getValue();
            }
        };
    }

    private HierarchyNode<T> ensureExistAndGet(T key) {
        HierarchyNode<T> element = this.data.get(key);

        if(element == null){
            throw new IllegalArgumentException();
        }

        return element;
    }
}