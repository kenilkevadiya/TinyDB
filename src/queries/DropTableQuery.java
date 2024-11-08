package queries;

public class DropTableQuery {
    private String tableName;

    public DropTableQuery(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "DropTableQuery{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}
