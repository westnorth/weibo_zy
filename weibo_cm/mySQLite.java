import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.portable.InputStream;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.seg.CJKSegmenter;

import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class mySQLite {

	private String DATABASE_CONNECTION;

	public mySQLite(String strDBConnection) {
		DATABASE_CONNECTION = strDBConnection;
	}

	public boolean insertParseText(String strFileName, String strTableName) {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DATABASE_CONNECTION);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			Scanner scannerStatus = new Scanner(new File(strFileName));
			while (scannerStatus.hasNext()) {
				FileInputStream fileStream = null;
				fileStream = new FileInputStream(strFileName);
				Reader reader = new InputStreamReader(fileStream);
				IKSegmentation mySegment = new IKSegmentation(reader);
				try {
					Lexeme nextLexeme = mySegment.next();
					while (nextLexeme != null) {
						String strSeg = nextLexeme.getLexemeText();
						System.out.println(strSeg);
						ResultSet rs = stat.executeQuery("insert into "
								+ strTableName + " values ('" + strSeg + "');");
						conn.commit();
						nextLexeme = mySegment.next();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 将用户信息插入数据库
	 * 
	 * @param strInfo
	 *            数组，1：ID，2：Access Token,3:Access secret
	 * @return 成功插入，返回真，否则为假
	 */
	@SuppressWarnings("finally")
	public boolean insertUserInfo(String[] strInfo) {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(DATABASE_CONNECTION);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("select * from userInfo where id = '"
							+ strInfo[0] + "';");
			String strSQLiteID = rs.getString("id");
			User userInfo = getUserInfo(strInfo);
			// String strUserInfo=userInfo.toString();
			// strUserInfo=strUserInfo.substring(5,strUserInfo.length()-1);

			if (strSQLiteID == null)// 如果该id还不存在于数据库中，将其插入
			{
				stat.executeUpdate("insert into userInfo(id,screenName,access_token,access_secret) values ('"
						+ strInfo[0]
						+ "','"
						+ userInfo.getScreenName()
						+ "','"
						+ strInfo[1] + "','" + strInfo[2] + "');");
				conn.commit();
			}

			// //以下一共替换了三个字符串，一是日期，一是id，一是false，给这三者都加上了引号，这样，
			// //执行插入操作时就不会报错了。这两个正则式匹配了日期及id
			String regEx = "createdAt=(.*),\\W?fa";
			String regEx2 = "id=(\\d+)";

			String strUserInfo = userInfo.toString().replaceAll(regEx,
					"createdAt='$1',fa");
			strUserInfo = strUserInfo.substring(5, strUserInfo.length() - 1);
			strUserInfo = strUserInfo.replaceAll(regEx2, "id='$1'");
			strUserInfo = strUserInfo.replaceAll("false", "'false'");
			String strExecute = "update userInfo set " + strUserInfo
					+ " where id = '" + strInfo[0] + "';";
			stat.executeUpdate(strExecute);
			conn.commit();
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public boolean insertStatus(List<Status> listStatus) {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(DATABASE_CONNECTION);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			ResultSet rs = null;
			for (int i = 0; i < listStatus.size(); i++) {
				String strStatusID = String.valueOf(listStatus.get(i).getId());
				String strStatusInfo = listStatus.get(0).toString();
				rs = stat.executeQuery("select * from statusInfo where id = '"
						+ strStatusID + "';");
				String strSQLiteID = rs.getString("id");

				if (strSQLiteID == null)// 如果该id还不存在于数据库中，将其插入
				{
					// stat.executeUpdate("insert into userInfo(id) values ('"
					// + strStatusID + "');");
					// conn.commit();
				}

				// //以下一共替换了三个字符串，一是日期，一是id，一是false，给这三者都加上了引号，这样，
				// //执行插入操作时就不会报错了。这两个正则式匹配了日期及id
				String regEx = "createdAt=(.*),\\W?fa";
				String regEx2 = "id=(\\d+)";

				String strUserInfo = strStatusInfo.toString().replaceAll(regEx,
						"createdAt='$1',fa");
				strUserInfo = strUserInfo
						.substring(5, strUserInfo.length() - 1);
				strUserInfo = strUserInfo.replaceAll(regEx2, "id='$1'");
				strUserInfo = strUserInfo.replaceAll("false", "'false'");
				String strExecute = "update userInfo set " + strUserInfo
						+ " where id = '" + strStatusID + "';";
				// stat.executeUpdate(strExecute);
				conn.commit();
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * this function create two Table,1.userInfo 2.statusInfo
	 */
	private void testSQLite() {
		Connection conn=null;
		try {
			Class.forName("org.sqlite.JDBC");

			conn = DriverManager.getConnection(DATABASE_CONNECTION);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			// User{weibo=null, id=2438418282, name='心情慵懒',
			// screenName='心情慵懒', location='甘肃 陇南', description='',
			// profileImageUrl='http://tp3.sinaimg.cn/2438418282/50/5612515360/1',
			// province='62', city='26', domain ='', gender ='m',
			// url='',
			// allowAllActMsg=false,
			// followersCount=1,
			// friendsCount=40, createdAt=Sun Oct 02 00:00:00 CST 2011,
			// favouritesCount=0, following=false, statusesCount=0,
			// geoEnabled=false,
			// voiderified=false,
			// status=null}

			stat.executeUpdate("create table userInfo ("
					+ "id PRIMARY KEY NOT NULL,"// 用户UID"，主键
					+ "screenName,"// 微博昵称
					+ "name,"// 友好显示名称，同微博昵称
					+ "province,"// 省份编码（参考省份编码表）
					+ "city,"// 城市编码（参考城市编码表）
					+ "location,"// 地址
					+ "description,"// 个人描述
					+ "url,"// 用户博客地址
					+ "profileImageUrl,"// 自定义图像
					+ "domain,"// 用户个性化URL
					+ "gender,"// 性别,m--男，f--女,n--未知
					+ "followersCount,"// 粉丝数
					+ "friendsCount,"// 关注数
					+ "statusesCount,"// 微博数
					+ "favouritesCount,"// 收藏数
					+ "createdAt,"// 创建时间
					+ "following,"// 是否已关注(此特性暂不支持)
					+ "verified,"// 加V标示，是否微博认证用户
					+ "status,"// 状态，由取回的字符中提取，意义不明
					+ "geoEnabled,"// 地理状态信息
					+ "allowAllActMsg,"// 由取回的字符中提取，意义不明
					+ "weibo,"// 由取回的字符中提取，意义不明
					+ "access_token,"// 访问Token
					+ "access_secret)");// 访问密钥

			// [createdAt=Sat Oct 01 23:51:54 CST 2011,
			// id=3363835379243442, text=养生之道，首在养气。,
			// source=<a href="http://mail.sina.com.cn"
			// rel="nofollow">新浪免费邮箱</a>,
			// isTruncated=false, inReplyToStatusId=-1, inReplyToUserId=-1,
			// isFavorited=false, inReplyToScreenName=,
			// latitude=-1.0,
			// longitude=-1.0,
			// thumbnail_pic=, bmiddle_pic=,
			// original_pic=,
			// mid=3363835379243442,
			// user=null,
			// retweeted_status=null]}

			stat.executeUpdate("create table statusInfo (" + "created_at,"// 创建时间
					+ "id PRIMARY KEY NOT NULL,"// 微博ID，主键
					+ "text,"// 微博信息内容
					+ "source,"// 微博来源
					+ "favorited,"// 是否已收藏
					+ "truncated,"// 是否被截断
					+ "in_reply_to_status_id,"// 回复ID
					+ "in_reply_to_user_id,"// 回复人UID
					+ "in_reply_to_screen_name,"// 回复人昵称
					+ "thumbnail_pic,"// 缩略图
					+ "bmiddle_pic,"// 中型图片
					+ "original_pic,"// 原始图片
					+ "user,"// 作者信息
					+ "retweeted_status)");// 转发的博文，内容为status，如果不是转发，则没有此字段
			conn.commit();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("create table error");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 由数据库的表中取出一列数据
	 * 
	 * @param strTable
	 *            表名称
	 * @param strColumn
	 *            列名称
	 * @return 数组，包含了该列数据
	 */
	public String[] getColumnDataFromDB(String strTable, String strColumn) {
		String[] strResult = null;
		Connection conn=null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(DATABASE_CONNECTION);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("SELECT COUNT(*) AS NumberOfUsers FROM "
							+ strTable + ";");
			String strNumber = rs.getString("NumberOfUsers").trim();
			if (strNumber == null || strNumber == "" || strNumber.equals("0"))
				return null;
			int nUser = Integer.parseInt(rs.getString("NumberOfUsers"));
			strResult = new String[nUser];
			ResultSet rs2 = stat.executeQuery("select " + strColumn + " from "
					+ strTable + ";");
			int i = 0;
			while (rs2.next()) {
				strResult[i] = rs2.getString(strColumn);
				i++;
			}
			stat.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return strResult;
	}

	/**
	 * 由数据库的表中取出一row数据
	 * 
	 * @param strTable
	 *            表名称
	 *  @param strElementName
	 *            name of Element which you want to compare 
	 * @param strRowValue
	 *            Condation ,if ElementName == strRowValue
	 * @return 数组，包含了该列数据
	 */
	public String[] getRowDataFromDB(String strTable,String strElementName,String strRowValue) {
		Connection conn=null;
		String strResult[]=new String[4];
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(DATABASE_CONNECTION);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
//			ResultSet rs = stat
//					.executeQuery("SELECT COUNT(*) AS NumberOfUsers FROM "
//							+ strTable + ";");
//			String strNumber = rs.getString("NumberOfUsers").trim();
//			if (strNumber == null || strNumber == "" || strNumber.equals("0"))
//				return null;
			String strQuery="select * from "
					+ strTable + " where "+strElementName +" = '"+strRowValue.trim()+" ';";
			ResultSet rs = stat.executeQuery(strQuery);
			if(rs.getString("id")!=null){
			strResult[0]=rs.getString("id");
			strResult[1]=rs.getString("name");
			strResult[2]=rs.getString("access_token");
			strResult[3]=rs.getString("access_secret");
			}
			stat.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return strResult;
	}
	
	/**
	 * get user information
	 * 
	 * @param strUserInfo
	 *            3 dim String ,1:user id 2:user token 3:user token secret.
	 * @return User ,includ all the information can get from weibo.com
	 */
	@SuppressWarnings("finally")
	public User getUserInfo(String[] strUserInfo) {
		Weibo weiboInfo = new Weibo();
		weiboInfo.setToken(strUserInfo[1], strUserInfo[2]);
		User user = null;
		try {
			user = weiboInfo.showUser(strUserInfo[0]);
			System.out.println("this user:" + user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		} finally {
			return user;
		}
	}
}