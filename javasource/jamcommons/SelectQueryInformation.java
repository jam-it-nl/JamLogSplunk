package jamcommons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mendix.core.Core;
import com.mendix.logging.LogLevel;

public class SelectQueryInformation extends AbstractWhereQueryInformation {

	private List<String> selectColumns = new ArrayList<String>();

	public SelectQueryInformation(String tableName) {
		super(tableName);
	}

	public void addSelectColumn(String key) {
		selectColumns.add(key);
	}

	public PreparedStatement getSelectPreparedStatement(Connection connection) throws SQLException {
		StringBuilder query = new StringBuilder("SELECT ");

		int parameterIndex = 1;
		for (String key : selectColumns) {
			query.append(key);

			if (parameterIndex != selectColumns.size()) {
				query.append(",");
			}

			parameterIndex++;
		}
		query.append(" FROM ");
		query.append(this.getTableName());
		query.append(" ");
		query.append(this.getWhereClause());

		String queryString = query.toString();
		Core.getLogger(this.getClass().getName()).log(LogLevel.DEBUG, queryString);
		PreparedStatement preparedStatement = connection.prepareStatement(queryString);

		parameterIndex = 1;
		for (Object value : this.getWhereColumnValues()) {
			preparedStatement.setObject(parameterIndex, value);
			parameterIndex++;
		}
		return preparedStatement;
	}
}
