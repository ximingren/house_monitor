package common;

import com.alibaba.fastjson.JSONObject;
import com.ximingren.house.common.dto.PageInfoDto;
import com.ximingren.house.common.entity.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private RoomExtDao roomExtDao;
    @Test
    public void testQueryAll() {
        List<Room> list = roomDao.queryAll(0, 100);
        for (Room room : list) {
            System.out.println(room);
        }
    }

    @Test
    public void testQueryWithPage() {
        JSONObject params = new JSONObject();
        params.put("pageNo", 2);
        params.put("pageSize",20);
        PageInfoDto pageInfoDto = JSONObject.parseObject(params.toJSONString(), PageInfoDto.class);
        List<Room> list = roomExtDao.queryWithPage(pageInfoDto);
        for (Room room : list) {
            System.out.println(room);
        }
    }
}
