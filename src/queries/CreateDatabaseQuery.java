package queries;

public class CreateDatabaseQuery {
    private String databaseName;

    public CreateDatabaseQuery(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return "CreateDatabaseQuery{" +
                "databaseName='" + databaseName + '\'' +
                '}';
    }
}
