import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import com.kuang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-18:40
 */

public class MyTest {


    @Test
    public void testCache(){
        //第一次查询
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
        User user1 = mapper1.queryUserById(1);
        System.out.println(user1);
        sqlSession1.close();
        //关闭第一次查询 内容放入二级缓存

        //第二次查询
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        User user2 = mapper2.queryUserById(1);
        System.out.println(user2);

        System.out.println(user1==user2);
        sqlSession2.close();
    }


    @Test
    public void queryUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //查询两次
        User user1 = mapper.queryUserById(1);
        System.out.println(user1);

        System.out.println("============");

        //两次查询增加修改操作
//        int i = mapper.updateUser(new User(2, "aaa", "bbb"));

        //手动清理缓存
        sqlSession.clearCache();
        User user2 = mapper.queryUserById(2);
        System.out.println(user2);

        System.out.println("一次会话两次查询同一个用户是否相同: "+(user1==user2));

        sqlSession.close();

    }
}
