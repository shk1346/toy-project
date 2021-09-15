package com.kh.toy.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class JDBCTemplate {
	
	//** Singleton 패턴
	// 해당 클래스의 인스턴스가 하나만 생성되어야 할 때 사용하는 디자인패턴
	
	//3) 필드변수에 JDBCTemplate 인스턴스 만들어주기
	private static JDBCTemplate instance;
	private static PoolDataSource pds;
	
	
	// 1) 생성자를 private으로 처리해, 외부에서 JDBTemplate을 생성하는 것을 차단.
	private JDBCTemplate() {
		//자주 사용되는 것들을 JDBCTamplate에 빼냄
		
		try {
			// 1. oracle jdbc Driver를 JVM에 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			 final String DB_URL="jdbc:oracle:thin:@pclassmdb_high?TNS_ADMIN=C:/CODE/Wallet_PCLASSMDB";
			  // Use the TNS Alias name along with the TNS_ADMIN - For ATP and ADW
			  // final static String DB_URL="jdbc:oracle:thin:@dbname_alias?TNS_ADMIN=/Users/test/wallet_dbname";
			  final String DB_USER = "ADMIN";
			  final String DB_PASSWORD = "sY8@88iytBRU4GJ";
			  final String CONN_FACTORY_CLASS_NAME="oracle.jdbc.pool.OracleDataSource";
			
			 pds = PoolDataSourceFactory.getPoolDataSource();
			 pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
			 pds.setURL(DB_URL);
			 pds.setUser(DB_USER);
			 pds.setPassword(DB_PASSWORD);
		     pds.setConnectionPoolName("JDBC_UCP_POOL");
		    
		    // Default is 0. Set the initial number of connections to be created
		    // when UCP is started.
		    pds.setInitialPoolSize(5);

		    // Default is 0. Set the minimum number of connections
		    // that is maintained by UCP at runtime.
		    pds.setMinPoolSize(5);

		    // Default is Integer.MAX_VALUE (2147483647). Set the maximum number of
		    // connections allowed on the connection pool.
		    pds.setMaxPoolSize(20);

		    // Default is 30secs. Set the frequency in seconds to enforce the timeout
		    // properties. Applies to inactiveConnectionTimeout(int secs),
		    // AbandonedConnectionTimeout(secs)& TimeToLiveConnectionTimeout(int secs).
		    // Range of valid values is 0 to Integer.MAX_VALUE. .
		    pds.setTimeoutCheckInterval(5);

		    // Default is 0. Set the maximum time, in seconds, that a
		    // connection remains available in the connection pool.
		    pds.setInactiveConnectionTimeout(10);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 2) 외부에서 JDBCTemplate의 인스턴스를 생성하지 않고도 사용할 수 있는 
	// JDBCTemplate의 인스턴스를 반환받는 용도의 메서드 (static)
	public static JDBCTemplate getInstance() {
		if(instance == null) { 
			//단 한 번도 instance라는 변수가 초기화된 적 없다면 (= 한 번도 생성된 적 없다면)
			instance = new JDBCTemplate(); //새로운 인스턴스 생성하여 instance에 할당
		}
		
		return instance;  //그게 아니라면 instance 반환
	}
	
	
	// 2. 데이터베이스와 연결 수립 
	public Connection getConnection() {
		
		Connection conn = null;
		
		try {
			
			conn = pds.getConnection();
			
			//Transaction 관리를 개발자가 하기 위해 AutoCommit 설정 끄기
			conn.setAutoCommit(false);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	// 3. commit, rollback (이때까진 자동으로 진행되었음)
	public void commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 4. 시스템 자원(Connection, Statement, ResultSet)을 반환
	public void close(Connection conn) {
		
		try {
			//매개변수로 넘어오는 connection이 null일 수도 있으므로 예외처리 해줘야 함
			//finally에서 넘어오기 때문에 null이 넘어올 수 있음
			//close처리가 되지 않았을 때 close처리
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(Statement stmt) {
		try {
			if(stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(ResultSet rset) {
		
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(Statement stmt, Connection conn) {
		close(stmt);
		close(conn);
	}
	
	public void close(ResultSet rset, Statement stmt) {
		close(rset);
		close(stmt);
	}
	
	public void close(ResultSet rset, Statement stmt, Connection conn) {
		close(rset);
		close(stmt);
		close(conn);
	}
	
}
