package P08Exams.P05Exam.core;

import P08Exams.P05Exam.models.Route;

import java.util.*;
import java.util.stream.Collectors;

public class MoovItImpl implements MoovIt {

    private Map<String,Route> routes;
    private Set<Route> routeSet;

    public MoovItImpl(){
        this.routes = new LinkedHashMap<>();
        this.routeSet = new HashSet<>();
    }

    @Override
    public void addRoute(Route route) {
        if (routeSet.contains(route)){
            throw new IllegalArgumentException();
        }
        routes.put(route.getId(),route);
        routeSet.add(route);
    }

    @Override
    public void removeRoute(String routeId) {
        if (!routes.containsKey(routeId)){
            throw new IllegalArgumentException();
        }
        Route route = routes.get(routeId);
        routes.remove(routeId);
        routeSet.remove(route);
    }

    @Override
    public boolean contains(Route route) {
        return routeSet.contains(route);
    }

    @Override
    public int size() {
        return routes.size();
    }

    @Override
    public Route getRoute(String routeId) {
        Route route = routes.get(routeId);
        if (route == null){
            throw new IllegalArgumentException();
        }

        return route;
    }

    @Override
    public void chooseRoute(String routeId) {
        Route route = routes.get(routeId);
        if (route == null){
            throw new IllegalArgumentException();
        }
        route.setPopularity(route.getPopularity() + 1);
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        List<Route> favoriteMatchingRoutes = routes.values().stream()
                .filter(route -> {
                    List<String> locations = route.getLocationPoints();
                    int startIndex = locations.indexOf(startPoint);
                    int endIndex = locations.indexOf(endPoint);
                    return startIndex != -1 && endIndex != -1 && startIndex < endIndex && route.getIsFavorite();
                })
                .sorted(Comparator
                        .comparingInt((Route route) -> {
                            List<String> locations = route.getLocationPoints();
                            int startIndex = locations.indexOf(startPoint);
                            int endIndex = locations.indexOf(endPoint);
                            return endIndex - startIndex;
                        })
                        .thenComparing(Route::getPopularity, Comparator.reverseOrder())
                        .thenComparing(Route::getDistance))
                .collect(Collectors.toList());

        List<Route> matchingRoutes = routes.values().stream()
                .filter(route -> {
                    List<String> locations = route.getLocationPoints();
                    int startIndex = locations.indexOf(startPoint);
                    int endIndex = locations.indexOf(endPoint);
                    return startIndex != -1 && endIndex != -1 && startIndex < endIndex && !route.getIsFavorite();
                })
                .sorted(Comparator
                        .comparingInt((Route route) -> {
                            List<String> locations = route.getLocationPoints();
                            int startIndex = locations.indexOf(startPoint);
                            int endIndex = locations.indexOf(endPoint);
                            return endIndex - startIndex;
                        })
                        .thenComparing(Route::getPopularity, Comparator.reverseOrder())
                        .thenComparing(Route::getDistance))
                .collect(Collectors.toList());
        favoriteMatchingRoutes.addAll(matchingRoutes);
        return favoriteMatchingRoutes;

    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        List<Route> favoriteRoutes = new ArrayList<>();

        for (Route route : routes.values()) {
            if (route.getIsFavorite() && route.getLocationPoints().indexOf(destinationPoint) > 0) {
                favoriteRoutes.add(route);
            }
        }
        favoriteRoutes.sort(Comparator
                .comparing(Route::getDistance)
                .thenComparing(Route::getPopularity, Comparator.reverseOrder()));

        return favoriteRoutes;
    }

    @Override
    public Iterable<Route> getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints() {
        List<Route> allRoutes = new ArrayList<>(routes.values());

        allRoutes.sort(Comparator
                .comparing(Route::getPopularity, Comparator.reverseOrder())
                .thenComparing(Route::getDistance)
                .thenComparing(route -> route.getLocationPoints().size()));

        return allRoutes.subList(0, Math.min(5, allRoutes.size()));
    }
}
