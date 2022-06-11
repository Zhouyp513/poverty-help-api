package cn.poverty.api.controller;

import cn.poverty.api.description.AuthDescriber;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author
 * 权限前端控制器
 * @projectName poverty-help-api
 * @title:corp.dataapi.controller-AuthController
 * @date 2019/4/24 11:25
 */
@RestController
@RequestMapping(value = "api/v1/auth")
public class AuthController extends BaseApiController implements AuthDescriber {




}
