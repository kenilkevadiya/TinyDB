package queries;

public class UseDatabaseQuery {
    private String databaseName;

    public UseDatabaseQuery(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return "UseDatabaseQuery{" +
                "databaseName='" + databaseName + '\'' +
                '}';
    }
}
