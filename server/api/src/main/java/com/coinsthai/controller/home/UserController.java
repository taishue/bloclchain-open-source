package com.coinsthai.controller.home;

import com.coinsthai.converter.IdCardViewConverter;
import com.coinsthai.converter.SimpleConverter;
import com.coinsthai.model.IdCard;
import com.coinsthai.model.User;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.service.UserService;
import com.coinsthai.vo.user.CellphoneUpdateRequest;
import com.coinsthai.vo.user.IdCardView;
import com.coinsthai.vo.user.PasswordUpdateRequest;
import com.coinsthai.vo.user.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 
 */
@Api(description = "用户API")
@RestController
@RequestMapping("/api/home/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleConverter<UserView, User> viewConverter;

    @Autowired
    private IdCardViewConverter idCardViewConverter;

    @ApiOperation(value = "获得当前登录用户信息")
    @GetMapping
    public UserView current() {
        User user = userService.get(SecurityUtils.currentUserId());
        UserView view = new UserView();
        viewConverter.toPojo(user, view);

        return view;
    }

    @ApiOperation(value = "获得当前登录用户的KYC身份证")
    @GetMapping("/id-card")
    public IdCardView getIdCard() {
        IdCard idCard = userService.getByUser(SecurityUtils.currentUserId());
        return idCardViewConverter.toPojo(idCard);
    }


    @ApiOperation(value = "修改用户手机")
    @PostMapping("/cellphone")
    public UserView updateCellphone(@RequestBody CellphoneUpdateRequest request) {
        request.setId(SecurityUtils.currentUserId());
        User user = userService.updateCellphone(request);
        UserView view = new UserView();
        viewConverter.toPojo(user, view);

        return view;
    }

    @ApiOperation(value = "修改用户密码")
    @PostMapping("/password")
    public UserView updatePassword(@RequestBody PasswordUpdateRequest request) {
        request.setId(SecurityUtils.currentUserId());
        User user = userService.updatePassword(request);
        UserView view = new UserView();
        viewConverter.toPojo(user, view);

        return view;
    }

}
