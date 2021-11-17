package jamcommons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteQueryInformation extends AbstractWhereQueryInformation {

	public DeleteQueryInformation(String tableName) {
		super(tableName);
	}

	public PreparedStatement getDeletePreparedStatement(Connection connection) throws SQLException {
		StringBuilder query = new StringBuilder("DELETE FROM ");
		query.append(this.getTableName());
		query.append(" ");
		query.append(this.getWhereClause());

		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());

		int parameterIndex = 1;
		for (Object value : this.getWhereColumnValues()) {
			preparedStatement.setObject(parameterIndex, value);
			parameterIndex++;
		}
		return preparedStatement;
	}
}
