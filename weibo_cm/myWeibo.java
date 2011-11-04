import java.util.ArrayList;
import java.util.List;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class myWeibo {
	private static String DATABASE_CONNECTION = "jdbc:sqlite:weibo.db";// 后面添加一个数据库名就是一个完整的连接字符串
	private Weibo weibo = new Weibo();

	public myWeibo(String consumKey, String consumSecret) {
		System.setProperty("weibo4j.oauth.consumerKey", consumKey);
		System.setProperty("weibo4j.oauth.consumerSecret", consumSecret);
	}

	/**
	 * 获得用户信息
	 * @param strToken 1.用户id，2,访问 Token，3.访问密钥
	 * @return 类型为User的一个变量，如果出错，返回null
	 * @exception WeiboException
	 */
	public User getUserInfo(String[] strToken) {
		Weibo weiboInfo = new Weibo();
		weiboInfo.setToken(strToken[1], strToken[2]);
		User user = null;
		try {
			user = weiboInfo.showUser(strToken[0]);
			System.out.println("this user:" + user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		} 
		return user;
	}
	
	/**
	 * 获得微博内容,要注意的是如果是Retweet的消息，则需要取得retweeted_status中的内容
	 * 
	 * @param strName
	 *            欲获得内容微博主人的名字或id
	 * @param strTokenKey
	 *            访问令牌
	 * @param strTokenSecret
	 *            访问密钥
	 * @param nPage
	 *            要获得内容的页码编号
	 * @param nNumberOfStatus
	 *            每页要获得的Status的数量
	 * @return 获得内容列表
	 */
	public ArrayList<statusTree> getStatus(String strName, String strTokenKey,
			String strTokenSecret, int nPage, int nNumberOfStatus) {
		ArrayList<statusTree> listTree = new ArrayList<statusTree>();
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(strTokenKey, strTokenSecret);
			List<Status> list = weibo.getUserTimeline(strName,
					new Paging(nPage).count(nNumberOfStatus));
			if (list.size() == 0) {
				System.out.println("there is no status");
			} else {
				for (int i = 0; i < list.size(); i++) {
					Status statusUser = list.get(i);
					statusTree myTree = new statusTree("0", statusUser);// root
																		// status
																		// the
																		// dad
																		// is
																		// zero
					String strDadId = String.valueOf(statusUser.getId());
					listTree.add(myTree);
					while (statusUser.getRetweeted_status() != null) {
						Status subStatus = statusUser.getRetweeted_status();
						statusTree subTree = new statusTree(strDadId, subStatus);
						listTree.add(subTree);
						statusUser = subStatus;
						strDadId = String.valueOf(statusUser.getId());
					}
					mySQLite myDB = new mySQLite(DATABASE_CONNECTION);
					myDB.insertStatus(list);
					String Text = statusUser.getText();
					System.out.println(Text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return listTree;
	}

	/**
	 * 获得粉丝列表
	 * @param strToken 1.id，2.Token,3.token secret
	 * @param nPage 页码
	 * @return粉丝列表的id数组，如果为空，返回null
	 */
	public long[] getFollowersIDs(String[] strToken,int nPage){
		long[] ids=null;
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(strToken[1],strToken[2]);
			//cursor处理翻页
			int cursor=nPage;
			//args[2]:关注用户的id
			ids = weibo.getFollowersIDSByUserId(strToken[0], cursor).getIDs();

		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return ids;
	}
	
	/**
	 * 关注某用户
	 * @param strToken 1.要关注用户的id，2.自己的Token，3.自己的Secret
	 * @return User类型的用户信息，失败，返回null
	 */
	public User followerSomeGuy(String[] strToken)
	{
		User returnUser=null;
		Weibo weibo=new Weibo();
		weibo.setToken(strToken[1],strToken[2]);
		try {
			returnUser=weibo.createFriendship(strToken[0]);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return returnUser;
	}
	
	/**
	 * 取消关注某用户
	 * @param strToken 1.要取消关注用户的id，2.自己欢迎各界人士联系合作的Token，3.自己的Secret
	 * @return User类型的用户信息，失败，返回null
	 */
	public User DestroyGuy(String[] strToken)
	{
		User returnUser=null;
		Weibo weibo=new Weibo();
		weibo.setToken(strToken[1],strToken[2]);
		try {
			returnUser=weibo.destroyFriendship(strToken[0]);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return returnUser;
	}
	
	
	public User blockGuy(String[] strToken)
	{
		User returnUser=null;
		Weibo weibo=new Weibo();
		weibo.setToken(strToken[1],strToken[2]);
		try {
			returnUser=weibo.block(strToken[0]);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return returnUser;
	}
}