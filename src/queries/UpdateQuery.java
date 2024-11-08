package queries;

import java.util.List;

public class UpdateQuery {
    private String tableName;
    private List<String> setColumn;
    private List<String> setValue;
    private String conditionColumn;
    private String conditionOperator;
    private String conditionValue;

    public UpdateQuery(String tableName, List<String> setColumn, List<String> setValue, String conditionColumn, String conditionOperator, String conditionValue) {
        this.tableName = tableName;
        this.setColumn = setColumn;
        this.setValue = setValue;
        this.conditionColumn = conditionColumn;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getSetColumn() {
        return setColumn;
    }

    public List<String> getSetValue() {
        return setValue;
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
        return "UpdateQuery{" +
                "tableName='" + tableName + '\'' +
                ", setColumn='" + setColumn + '\'' +
                ", setValue='" + setValue + '\'' +
                ", conditionColumn='" + conditionColumn + '\'' +
                ", conditionOperator='" + conditionOperator + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                '}';
    }
}
