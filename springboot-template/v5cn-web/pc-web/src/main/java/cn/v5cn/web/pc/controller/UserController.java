package cn.v5cn.web.pc.controller;

import cn.v5cn.web.common.Result;
import cn.v5cn.web.pc.entity.SysUser;
import cn.v5cn.web.pc.groups.ValidationInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zyw
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    private static final List<SysUser> USERS = new ArrayList<>();

    // 数据初始化
    static {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("zhangsan");
        user.setPhone("13566666666");
        user.setEmail("example@qq.com");
        USERS.add(user);
        SysUser user1 = new SysUser();
        user1.setId(2L);
        user1.setUsername("lisi");
        user1.setPhone("13588888888");
        user1.setEmail("example1@qq.com");
        USERS.add(user1);
    }

    /*********************************
     * 使用统一异常处理（查看GlobalResultHandler类）可以去掉每个方法中的BindingResult参加
     * ******************************/

    /**
     * 根据手机号或邮箱查询用户信息
     * @param sysUser 查询条件
     * @return 用户list
     */
    @GetMapping
    public Result queryList(@Validated(value = ValidationInterface.Select.class) SysUser sysUser,
                                  BindingResult result)
    {
        FieldError fieldError = result.getFieldError();

        if(Objects.nonNull(fieldError)) {
            return Result.error().message(getErrorMsg(fieldError));
        }

        String phone = sysUser.getPhone();
        String email = sysUser.getEmail();

        if(phone == null && email == null) {
            return Result.ok().data(USERS);
        }

        List<SysUser> queryResult = USERS.stream()
                .filter(obj -> obj.getPhone().equals(phone) || obj.getEmail().equals(email))
                .collect(Collectors.toList());
        return Result.ok().data(queryResult);
    }

    /**
     * 新增用户信息
     * @param sysUser 用户信息
     * @return 成功标识
     */
    @PostMapping
    public Result add(@Validated(value = ValidationInterface.Add.class)
                            @RequestBody SysUser sysUser,
                            BindingResult result)
    {
        FieldError fieldError = result.getFieldError();

        if(Objects.nonNull(fieldError)) {
            return Result.error().message(getErrorMsg(fieldError));
        }

        Long id = (long) (USERS.size() + 1);
        sysUser.setId(id);
        USERS.add(sysUser);
        return Result.ok().message("新增成功");
    }

    /**
     * 根据Id更新用户信息
     * @param sysUser 用户信息
     * @return 成功标识
     */
    @PutMapping("{id}")
    public Result updateById(@PathVariable("id") Long id,
                                   @Validated(value = ValidationInterface.Update.class)
                                   @RequestBody SysUser sysUser,
                                   BindingResult result)
    {
        FieldError fieldError = result.getFieldError();

        if(Objects.nonNull(fieldError)) {
            return Result.error().message(getErrorMsg(fieldError));
        }

        for(int i = 0; i < USERS.size(); i++) {
            if(USERS.get(i).getId().equals(id)) {
                USERS.set(i,sysUser);
            }
        }
        return Result.ok().message("更新成功");
    }

    /**
     * 根据Id删除用户信息
     * @param id 主键
     * @return 成功标识
     */
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable Long id) {
        USERS.removeIf(obj -> obj.getId().equals(id));
        return Result.ok().message("删除成功");
    }

    /**
     * 获取表单验证错误msg
     * @param fieldError 报错字段
     * @return msg
     */
    public String getErrorMsg(FieldError fieldError) {
        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String msg = "[" + fieldError.getDefaultMessage() + "]";
        log.error("{}：字段=={}\t值=={}", msg, field, rejectedValue);
        return msg;
    }
}
