package P08Exams.P04Exam.core;

import P08Exams.P04Exam.models.Package;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class PackageManagerImpl implements PackageManager {

    private final ConcurrentMap<String, Package> packageManager;
    private final ConcurrentMap<String, Set<String>> dependencies;
    private final ConcurrentMap<String, Set<String>> reverseDependencies;
    private final ConcurrentMap<String, Package> packageIndex;

    public PackageManagerImpl() {
        this.packageManager = new ConcurrentHashMap<>();
        this.dependencies = new ConcurrentHashMap<>();
        this.reverseDependencies = new ConcurrentHashMap<>();
        this.packageIndex = new ConcurrentHashMap<>();
    }

    @Override
    public void registerPackage(Package _package) {

        Package existingPackage = packageIndex.putIfAbsent(
                getPackageKey(_package.getName(), _package.getVersion()), _package);

        if (existingPackage == null) {
            packageManager.putIfAbsent(_package.getId(), _package);
            dependencies.put(_package.getId(), ConcurrentHashMap.newKeySet());
        } else {
            throw new IllegalArgumentException("Package with the same name and version already exists");
        }
    }

    private String getPackageKey(String name, String version) {
        return name + ":" + version;
    }
    @Override
    public void removePackage(String packageId) {
        if (!packageManager.containsKey(packageId)) {
            throw new IllegalArgumentException("No such package with id: " + packageId);
        }
        Package removedPackage = packageManager.remove(packageId);
        packageIndex.remove(getPackageKey(removedPackage.getName(), removedPackage.getVersion()));
        Set<String> dependants = reverseDependencies.remove(packageId);
        if (dependants != null) {
            dependants.forEach(dependantId -> dependencies.get(dependantId).remove(packageId));
        }
        dependencies.remove(packageId);
        packageManager.remove(packageId);
    }

    @Override
    public void addDependency(String packageId, String dependencyId) {
        if (!packageManager.containsKey(packageId) || !packageManager.containsKey(dependencyId)) {
            throw new IllegalArgumentException("Either one of the packages does not exist.");
        }
        dependencies.computeIfAbsent(packageId, k -> ConcurrentHashMap.newKeySet()).add(dependencyId);
        reverseDependencies.computeIfAbsent(dependencyId, k -> ConcurrentHashMap.newKeySet()).add(packageId);
    }

    @Override
    public boolean contains(Package _package) {
        return packageManager.containsKey(_package.getId());
    }

    @Override
    public int size() {
        return packageManager.size();
    }

    @Override
    public Iterable<Package> getDependants(Package _package) {
        String packageId = _package.getId();
        Set<String> dependantIds = reverseDependencies.getOrDefault(packageId, Collections.emptySet());

        return dependantIds.stream()
                .map(packageManager::get)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Package> getIndependentPackages() {
        return packageManager.values().parallelStream()
                .filter(pkg -> dependencies.getOrDefault(pkg.getId(), Collections.emptySet()).isEmpty())
                .sorted(Comparator.comparing(Package::getReleaseDate).reversed()
                        .thenComparing(Comparator.comparing(Package::getVersion)))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Package> getOrderedPackagesByReleaseDateThenByVersion() {
        return packageManager.values().stream()
                .collect(Collectors.toMap(Package::getName,
                        p -> p,
                        BinaryOperator.maxBy(Comparator.comparing(Package::getVersion))))
                .values().stream()
                .sorted(Comparator.comparing(Package::getReleaseDate).reversed()
                        .thenComparing(Comparator.comparing(Package::getVersion)))
                .collect(Collectors.toList());
    }
}