package entities;

import java.io.*;
import java.util.*;

public class ForeignKey {
    String tableName;
    List<String> foreignKeys;
    List<String> referenceTables;
    List<String> referenceTableColumns;

    public ForeignKey(String tableName, List<String> foreignKeys, List<String> referenceTables, List<String> referenceTableColumns) {
        this.tableName = tableName;
        this.foreignKeys = foreignKeys;
        this.referenceTables = referenceTables;
        this.referenceTableColumns = referenceTableColumns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getForeignKeys() {
        return foreignKeys;
    }

    public List<String> getReferenceTables() {
        return referenceTables;
    }

    public List<String> getReferenceTableColumns() {
        return referenceTableColumns;
    }

    public String serialize(String databaseName, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Database: ").append(databaseName).append("\n");
        sb.append("Table: ").append(tableName).append("\n");
        sb.append("ForeignKeys: ").append(String.join(",", foreignKeys)).append("\n");
        sb.append("ReferenceTables: ").append(String.join(",", referenceTables)).append("\n");
        sb.append("ReferenceTableColumns: ").append(String.join(",", referenceTableColumns)).append("\n");
        return sb.toString();
    }

    public void saveToFile(String filename, String databaseName, String tableName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(serialize(databaseName, tableName));
        }
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            deserialize(sb.toString());
        }
    }

    public void deserialize(String str) {
        String[] lines = str.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("ForeignKeys: ")) {
                foreignKeys = Arrays.asList(line.trim().substring(13).split(","));
            } else if (line.trim().startsWith("ReferenceTables: ")) {
                referenceTables = Arrays.asList(line.trim().substring(17).split(","));
            } else if (line.trim().startsWith("ReferenceTableColumns: ")) {
                referenceTableColumns = Arrays.asList(line.trim().substring(22).split(","));
            }
        }
    }

    public void generateForeignKeyMetadata(String filename, String databaseName, String tableName) throws IOException {
        if (!foreignKeys.isEmpty()) {
            saveToFile(filename, databaseName, tableName);
        }
    }
}
