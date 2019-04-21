package com.ximingren.house.other.impl;

import com.ximingren.house.common.entity.Room;
import common.RoomDao;
import com.ximingren.house.other.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//为什么要用这个注释
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDao roomDao;

    public List<Room> getRoomList(int offset, int limit) {
        List<Room> result = roomDao.queryAll(offset, limit);
        return result;
    }
}
