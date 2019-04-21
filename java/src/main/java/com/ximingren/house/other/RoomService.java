package com.ximingren.house.other;

import com.ximingren.house.common.entity.Room;

import java.util.List;

//为什么要用interface
public interface RoomService {
    List<Room> getRoomList(int offset, int limit);
}
