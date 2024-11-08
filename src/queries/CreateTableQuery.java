package queries;

import entities.ForeignKey;
import java.util.List;

public class CreateTableQuery {
    private String tableName;
    private List<String> columnNames;
    private List<String> columnDataTypes;
    private List<Integer> columnSizes;
    private String primaryKeyColumn;
    private ForeignKey foreignKey;

    public CreateTableQuery(String tableName, List<String> columnNames, List<String> columnDataTypes, List<Integer> columnSizes,
                            String primaryKeyColumn, ForeignKey foreignKey) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnDataTypes = columnDataTypes;
        this.columnSizes = columnSizes;
        this.primaryKeyColumn = primaryKeyColumn;
        this.foreignKey = foreignKey;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getColumnDataTypes() {
        return columnDataTypes;
    }

    public List<Integer> getColumnSizes() {
        return columnSizes;
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public ForeignKey getForeignKey() {
        return foreignKey;
    }
}


