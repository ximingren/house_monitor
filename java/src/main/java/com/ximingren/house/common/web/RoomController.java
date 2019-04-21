package com.ximingren.house.common.web;

import com.ximingren.house.common.entity.Room;
import com.ximingren.house.other.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/zufang")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "/list")
    public String list(Model model, Integer offset, Integer limit) {
        offset = offset == null ? 0 : offset;
        limit = limit == null ? 50 : limit;
        List<Room> list = roomService.getRoomList(offset, limit);
        model.addAttribute("roomList", list);
        return "roomlist";
    }
}
