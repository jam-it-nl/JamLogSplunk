package jamcommons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class UpdateQueryInformation extends AbstractWhereQueryInformation {

	private SortedMap<String, Object> columnValues = new TreeMap<String, Object>();

	public UpdateQueryInformation(String tableName) {
		super(tableName);
	}

	public void addColumnValue(String key, Object value) {
		columnValues.put(key, value);
	}

	public Object getColumnValue(String name) {
		return columnValues.get(name);
	}

	public Set<String> getColumnNames() {
		return columnValues.keySet();
	}

	public int getColumnValueSize() {
		return columnValues.size();
	}

	public Collection<Object> getColumnValues() {
		return columnValues.values();
	}

	public Set<Entry<String, Object>> getColumnEntrySet() {
		return columnValues.entrySet();
	}

	public String getCreateTableStatement() throws SQLException {
		StringBuilder query = new StringBuilder("CREATE TABLE ");
		query.append(this.getTableName());
		query.append("(");

		int parameterIndex = 1;
		for (String key : this.getColumnNames()) {
			query.append(key);
			query.append(" VARCHAR(1000)");

			if (parameterIndex != this.getColumnValueSize()) {
				query.append(",");
			}

			parameterIndex++;
		}
		query.append(");");
		return query.toString();
	}

	public PreparedStatement getInsertPreparedStatement(Connection connection) throws SQLException {
		int parameterIndex;
		String query = getInsertQuery();

		PreparedStatement preparedStatement = connection.prepareStatement(query);

		parameterIndex = 1;
		for (Object value : this.getColumnValues()) {
			preparedStatement.setObject(parameterIndex, this.convertPreparedStatementObject(value));
			parameterIndex++;
		}
		return preparedStatement;
	}

	public String getInsertQuery() {
		StringBuilder query = new StringBuilder("INSERT INTO ");
		query.append(this.getTableName());
		query.append(" (");

		int parameterIndex = 1;
		StringBuilder queryValues = new StringBuilder("VALUES (");
		for (String key : this.getColumnNames()) {
			query.append(key);

			queryValues.append("?");

			if (parameterIndex != this.getColumnValueSize()) {
				query.append(",");
				queryValues.append(",");
			}

			parameterIndex++;
		}
		query.append(") ");
		query.append(queryValues);
		query.append(");");
		return query.toString();
	}

	public PreparedStatement getUpdatePreparedStatement(Connection connection) throws SQLException {
		StringBuilder query = new StringBuilder("UPDATE ");
		query.append(this.getTableName());
		query.append(" SET ");

		int parameterIndex = 1;
		for (String key : this.getColumnNames()) {
			query.append(key);
			query.append(" = ?");

			if (parameterIndex != this.getColumnValueSize()) {
				query.append(",");
			}

			parameterIndex++;
		}
		query.append(" ");
		query.append(this.getWhereClause());

		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());

		parameterIndex = 1;
		for (Object value : this.getColumnValues()) {
			preparedStatement.setObject(parameterIndex, this.convertPreparedStatementObject(value));
			parameterIndex++;
		}
		for (Object value : this.getWhereColumnValues()) {
			preparedStatement.setObject(parameterIndex, this.convertPreparedStatementObject(value));
			parameterIndex++;
		}
		return preparedStatement;
	}

	private Object convertPreparedStatementObject(Object value) {

		if (value instanceof java.util.Date) {
			return new java.sql.Date(((java.util.Date) value).getTime());
		}

		if (value instanceof Integer) {
			return new Long((Integer) value);
		}

		return value;
	}

}
