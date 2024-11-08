package entities;

import Utils.QueryLogger;

import java.util.ArrayList;
import java.util.List;

public class Database implements Cloneable {

    String databaseName;

    List<Table> tables;

    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new ArrayList<>();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void addTable(String tableName, List<String> columnNames, List<String> columnDataTypes, List<Integer> columnSizes, String primaryKeyColumnName) {
        boolean isTableExists = isTablePresent(tableName);
        if (isTableExists) {
            return;
        }
        Table table = new Table(tableName);
        int i = 0;
        int size = columnNames.size();
        for (String columnName : columnNames) {
            if (i < size) {
                table.addColumn(columnName, columnDataTypes.get(i), columnSizes.get(i), primaryKeyColumnName);
                i++;
            }
        }
        tables.add(table);
        System.out.println("Successfully added the table");
    }

    public String dropTable(String tableName) {
        boolean isTableExists = isTablePresent(tableName);
        if (isTableExists) {
            for (Table table : tables) {
                if (table.getTableName().equalsIgnoreCase(tableName)) {
                    tables.remove(table);
                    break;
                }
            }
            return "1";
        } else {
            return "-1";
        }
    }

    private boolean isTablePresent(String tableName) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }

    public String insertData(String tableName, List<String> columnName, List<String> values) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                String result = table.insertData(columnName, values);
                return result;
            }
        }
        return "Failed Operation";
    }

    public List<Row> selectData(String tableName, List<String> columnNames, String columnName, String value) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                List<Row> rows = table.getDataFromTable(columnNames, columnName, value);
                return rows;
            }
        }
        return null;
    }

    public String updateData(String tableName, List<String> columnNames, List<String> values, String whereColumn, String whereValue) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                String result = table.updateData(columnNames, values, whereColumn, whereValue);
                return result;
            }
        }
        return null;
    }

    public String deleteData(String tableName, String columnName, String value) {
        List<Row> dataToBeDeleted = selectData(tableName, null, columnName, value);
        if (dataToBeDeleted.isEmpty()) {
            return "The given criteria records are not present";
        }
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                String result = table.deleteData(columnName, value);
                return result;
            }
        }
        return "Successfully deleted the Given Records with required condition";
    }

    // Serialize the Database
    public String serialize(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- Database: ").append(databaseName).append("\n");
        for (Table table : tables) {
            sb.append(table.serialize(indent + "  "));
        }
        return sb.toString();
    }

    // Deserialize the Database
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            if (parts[0].trim().equals("- Database")) {
                databaseName = parts[1].trim();
            } else {
                if (!tables.isEmpty()) {
                    tables.get(tables.size() - 1).deserialize(str);
                } else {
                    Table table = new Table("");
                    table.deserialize(str);
                    tables.add(table);
                }
            }
        }
    }

    public void LogDatabaseState(){
        for (Table table : tables) {

        }
        QueryLogger.logGeneral("TABLE COUNT", "");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Database cloned = (Database) super.clone();
        cloned.tables = new ArrayList<>(this.tables.size());
        for (Table table : this.tables) {
            cloned.tables.add((Table) table.clone());
        }
        return cloned;
    }

    public void logDatabaseState(String databaseName) {
        StringBuilder dbState = new StringBuilder();
        dbState.append("Database Name: ").append(databaseName).append("\n");
        dbState.append("Number of Tables: ").append(tables.size()).append("\n");

        for (Table table : tables) {
            dbState.append("Table Name: ").append(table.getTableName()).append("\n");
            dbState.append("Number of Records: ").append(table.data.size()).append("\n");
        }

        QueryLogger.logGeneral("DATABASE STATE", dbState.toString());
    }
}
