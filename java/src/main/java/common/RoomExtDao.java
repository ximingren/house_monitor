package common;

import com.ximingren.house.common.dto.PageInfoDto;
import com.ximingren.house.common.entity.Room;

import java.util.List;

public interface RoomExtDao {
    List<Room> queryWithPage(PageInfoDto pageInfoDto);
}
