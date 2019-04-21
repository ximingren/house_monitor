package common;

import com.ximingren.house.common.entity.Room;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomDao {
    List<Room> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}

