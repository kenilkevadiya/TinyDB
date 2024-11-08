package queries;

import java.util.List;

public class InsertQuery {
    private String tableName;
    private List<String> columnNames;
    private List<String> values;

    public InsertQuery(String tableName, List<String> columnNames, List<String> values) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "InsertQuery{" +
                "tableName='" + tableName + '\'' +
                ", columnNames=" + columnNames +
                ", values=" + values +
                '}';
    }
}
