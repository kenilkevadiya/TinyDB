package queries;

public class DeleteQuery {
    private String tableName;
    private String conditionColumn;
    private String conditionValue;
    private String conditionOperator;

    public DeleteQuery(String tableName, String conditionColumn, String conditionValue, String conditionOperator) {
        this.tableName = tableName;
        this.conditionColumn = conditionColumn;
        this.conditionValue = conditionValue;
        this.conditionOperator = conditionOperator;
    }

    public String getTableName() {
        return tableName;
    }

    public String getConditionColumn() {
        return conditionColumn;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public String getConditionOperator() {
        return conditionOperator;
    }

    @Override
    public String toString() {
        return "DeleteQuery{" +
                "tableName='" + tableName + '\'' +
                ", conditionColumn='" + conditionColumn + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                ", conditionOperator='" + conditionOperator + '\'' +
                '}';
    }
}
