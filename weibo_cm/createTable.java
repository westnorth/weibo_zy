import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class createTable {
	private static String DATABASE_CONNECTION = "jdbc:sqlite:weibo.db";

		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					createTable();
				}
			});
		}
		
		private static void createTable(){	
		Connection conn;
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
			// stat.executeUpdate("insert into people values ('Gandhi', 'politics');");
			// stat.executeUpdate("insert into people values ('Turing', 'computers');");
			// stat.executeUpdate("insert into people values ('Wittgenstein', 'smartypants');");
			conn.commit();
			//
			// ResultSet rs = stat.executeQuery("select * from people;");
			// while (rs.next()) {
			// System.out.println("name = " + rs.getString("name"));
			// System.out
			// .println("occupation = " + rs.getString("occupation"));
			// }

			// rs.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("create table error");
			e.printStackTrace();
		}
	}
}