package com.ztesoft.mybatis.model;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ztesoft.mybatis.inter.IUserOperation;

public class Test {
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	static {
		try {
			reader = Resources.getResourceAsReader("config/Configuration.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SqlSessionFactory getSession() {
		return sqlSessionFactory;
	}

	public static void getUserList(String userName) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			IUserOperation userOperation = session
					.getMapper(IUserOperation.class);
			List<User> users = userOperation.selectUsers(userName);
			for (User user : users) {
				System.out.println(user.getId() + ":" + user.getUserName() + ":" + user.getUserAddress());
			}

		} finally {
			session.close();
		}
	}
	
	public static void addUser(){
        User user=new User();
        user.setUserAddress("人民广场");
        user.setUserName("飞鸟");
        user.setUserAge("80");
        user.setId(3);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            IUserOperation userOperation=session.getMapper(IUserOperation.class);
            userOperation.addUser(user);
            session.commit();
            System.out.println("当前增加的用户 id为:"+user.getId());
        } finally {
            session.close();
        }
    }
    
	public static void updateUser(){
        //先得到用户,然后修改，提交。
        SqlSession session = sqlSessionFactory.openSession();
        try {
            IUserOperation userOperation=session.getMapper(IUserOperation.class);
            User user = userOperation.selectUserByID(3);            
            user.setUserAddress("原来是魔都的浦东创新园区");
            userOperation.updateUser(user);
            session.commit();
        } finally {
        	session.close();
        }
	}
	
    /**
     * 删除数据，删除一定要 commit.
     * @param id
     */
    public static void deleteUser(int id){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            IUserOperation userOperation=session.getMapper(IUserOperation.class);           
            userOperation.deleteUser(id);
            session.commit();            
        } finally {
            session.close();
        }
    }
    
    public static void getUserArticles(int userid){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            IUserOperation userOperation=session.getMapper(IUserOperation.class);           
            List<Article> articles = userOperation.getUserArticles(userid);
            for(Article article:articles){
                System.out.println(article.getTitle()+":"+article.getContent()+
                        ":作者是:"+article.getUser().getUserName()+":地址:"+
                         article.getUser().getUserAddress());
            }
        } finally {
            session.close();
        }
    }
    
    public static void dynamicForeachTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            IUserOperation userOperation=session.getMapper(IUserOperation.class);       
            List<Integer> ids = new ArrayList<Integer>();
            ids.add(1);
            ids.add(3);
            ids.add(2);
            List<Article> articles = userOperation.dynamicForeachTest(ids);
        	for(Article article:articles){
        		System.out.println(article.getTitle()+":"+article.getContent()+
                        ":作者是:"+article.getUser().getUserName()+":地址:"+
                         article.getUser().getUserAddress());
        	}
        }finally {
            session.close();
        }
    }
        
	public static void main(String[] args) {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			User user = (User) session.selectOne(
//					"com.ztesoft.mybatis.inter.IUserOperation.selectUserByID",
//					2);
//			System.out.println(user.getUserAddress());
//			System.out.println(user.getUserName());
//		} finally {
//			session.close();
//		}

//		// --------------------------------
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			IUserOperation userOperation = session
//					.getMapper(IUserOperation.class);
//			User user = userOperation.selectUserByID(1);
//			System.out.println(user.getUserAddress());
//			System.out.println(user.getUserName());
//		} finally {
//			session.close();
//		}

		// //----------------------------------
//		Test.getUserList("%");

		// //------------------------------------
//		Test.addUser();
		
//		----------------------------------
//		Test.updateUser();
		
		//---------------------------------
//		Test.deleteUser(3);
		
		//---------------------------------
//		Test.getUserArticles(1);
		
		////---------------------------------
		Test.dynamicForeachTest();
	}
}
