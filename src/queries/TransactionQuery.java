package queries;

public class TransactionQuery {
    private String statement;

    public TransactionQuery(String statement) {
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }
}
