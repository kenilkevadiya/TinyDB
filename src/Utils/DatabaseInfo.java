package Utils;

import java.util.*;

public class DatabaseInfo {
    private final Map<String, Set<String>> tablePrimaryKeys;

    public DatabaseInfo() {
        tablePrimaryKeys = new HashMap<>();
    }

    public void addPrimaryKey(String table, String primaryKey) {
        tablePrimaryKeys.computeIfAbsent(table, k -> new HashSet<>()).add(primaryKey.trim());
    }

    public boolean isPrimaryKey(String table, String column) {
        return tablePrimaryKeys.getOrDefault(table, Collections.emptySet()).contains(column.trim());
    }

    public void printPrimaryKeys() {
        for (Map.Entry<String, Set<String>> entry : tablePrimaryKeys.entrySet()) {
            System.out.println("Table: " + entry.getKey() + " Primary Keys: " + entry.getValue());
        }
    }
}