package com.zetalabs.indumelec.api.controller;

import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.service.UserService;
import com.zetalabs.indumelec.utils.FormUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiUserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/api/user/list")
    public Map<String, Object> companyList(@RequestParam("userId") String userId) {
        Map<String, Object> result = new HashMap<>();
        User loggedUser = userService.getUserByMail(userId);
        List<User> userList = userService.getUserList(loggedUser);

        result.put("data", userList);
        result.put("draw", 1);
        result.put("recordsTotal", userList.size());
        result.put("recordsFiltered", userList.size());

        return result;
    }

    @RequestMapping("/api/user/save")
    public ResponseEntity edit(@RequestParam("userId") String userId, @RequestParam("frmInfo") String frmInfo) {
        ResponseEntity responseEntity = null;
        User loggedUser = userService.getUserByMail(userId);
        User user = getUser(frmInfo);

        if (user.getUserId()==null && userService.getUserByMail(user.getEmail())!=null) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuario ya existe");
        } else {
            responseEntity = ResponseEntity.ok(HttpStatus.OK);
            userService.save(loggedUser, user);
        }

        return responseEntity;
    }

    @RequestMapping("/api/user/edit")
    public User edit(@RequestParam(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @RequestMapping("/api/user/delete")
    public ResponseEntity delete(@RequestParam(name = "userId") Long userId) {
        ResponseEntity responseEntity = null;

        try {
            responseEntity = ResponseEntity.ok(HttpStatus.OK);
            userService.delete(userId);
        } catch (Exception ex) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuario utilizado en cotizaciones. No puede ser eliminado");
        }

        return responseEntity;
    }

    private User getUser(String frmInfo) {
        User user = new User();

        Map<String, String> map = FormUtils.getMap(new JSONArray(frmInfo));
        String userId = map.get("userId");

        if (!StringUtils.isEmpty(userId)) {
            user.setUserId(Long.valueOf(userId));
        }

        user.setName(map.get("name"));
        user.setEmail(map.get("email"));
        user.setPassword(map.get("password"));
        user.setRole(Integer.valueOf(map.get("role")));

        return user;
    }
}
