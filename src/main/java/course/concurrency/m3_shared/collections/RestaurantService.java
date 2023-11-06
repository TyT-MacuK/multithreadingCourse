package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RestaurantService {

    private Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private Map<String, Long> stat = new ConcurrentHashMap<>() {{
        put("A", 0L);
        put("B", 0L);
        put("C", 0L);
    }};

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        stat.merge(restaurantName, 1L, (k,v) -> k + 1);
    }

    public Set<String> printStat() {
        return stat.entrySet().stream().map(val -> val.getKey() + " - " + val.getValue()).collect(Collectors.toSet());
    }
}
