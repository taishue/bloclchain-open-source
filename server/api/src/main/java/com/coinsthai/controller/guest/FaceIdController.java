package com.coinsthai.controller.guest;

import com.coinsthai.model.User;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.module.kyc.FaceIdService;
import com.coinsthai.service.UserService;
import com.coinsthai.util.JsonUtils;
import com.coinsthai.vo.kyc.FaceIdToken;
import com.coinsthai.module.kyc.FaceIdTokenRequest;
import com.coinsthai.vo.kyc.FaceIdVerifyResponse;
import com.coinsthai.module.kyc.rest.FaceIdNotifyResultRaw;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author
 */
@Api(description = "币种API")
@RestController
@RequestMapping("/api/faceid")
public class FaceIdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceIdController.class);

    @Autowired
    private FaceIdService faceIdService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "已登录的用户请求获取FaceID认证Token")
    @PostMapping("/token")
    public FaceIdToken requestToken() {
        FaceIdTokenRequest request = new FaceIdTokenRequest();
        request.setUserId(SecurityUtils.currentUserId());
        return faceIdService.requestToken(request);
    }

    @ApiOperation(value = "从PC端扫码过来的请求获取FaceID认证Token")
    @PostMapping("/token/{id}")
    public FaceIdToken requestToken(@PathVariable String id) {
        FaceIdTokenRequest request = new FaceIdTokenRequest();
        request.setId(id);
        return faceIdService.requestToken(request);
    }

    @ApiOperation(value = "响应FaceID的验证通知")
    @PostMapping("/notify")
    public void handleNotify(HttpServletRequest request) throws IOException {
        String raw = IOUtils.toString(request.getInputStream(), "UTF-8");
        LOGGER.info("Notify Raw: \n" + raw);
        FaceIdNotifyResultRaw resultRaw = JsonUtils.parseJson(raw, FaceIdNotifyResultRaw.class);
        faceIdService.handleNotify(resultRaw);
    }

    @ApiOperation(value = "验证是否通过FaceID认证")
    @PostMapping("/verify/{id}")
    public FaceIdVerifyResponse verify(@PathVariable String id, HttpServletRequest request) throws IOException {
        FaceIdVerifyResponse response = new FaceIdVerifyResponse();
        response.setId(id);

        String userId = SecurityUtils.currentUserId();
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.get(userId);
            response.setVerified(user.isBioVerify());
        }
        else {
            String raw = IOUtils.toString(request.getInputStream(), "UTF-8");
            LOGGER.info("Return Raw: \n" + raw);
            FaceIdNotifyResultRaw resultRaw = JsonUtils.parseJson(raw, FaceIdNotifyResultRaw.class);
            response.setVerified(faceIdService.verify(id, resultRaw));
        }

        return response;
    }

//    @ApiOperation(value = "测试获取照片")
//    @PostMapping("/result/{id}")
//    public void requestResult(@PathVariable String id) {
//        faceIdService.requestResult(id);
//    }

}
