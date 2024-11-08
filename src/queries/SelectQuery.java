package queries;

import java.util.List;

public class SelectQuery {
    private String tableName;
    private List<String> columns;
    private String conditionColumn;
    private String conditionOperator;
    private String conditionValue;

    public SelectQuery(String tableName, List<String> columns, String conditionColumn, String conditionOperator, String conditionValue) {
        this.tableName = tableName;
        this.columns = columns;
        this.conditionColumn = conditionColumn;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getConditionColumn() {
        return conditionColumn;
    }

    public String getConditionOperator() {
        return conditionOperator;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    @Override
    public String toString() {
        return "SelectQuery{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", conditionColumn='" + conditionColumn + '\'' +
                ", conditionOperator='" + conditionOperator + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                '}';
    }
}
