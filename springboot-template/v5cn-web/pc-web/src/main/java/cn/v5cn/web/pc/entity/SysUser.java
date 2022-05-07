package cn.v5cn.web.pc.entity;

import cn.v5cn.web.common.validate.Phone;
import cn.v5cn.web.pc.groups.ValidationInterface;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户信息管理
 * @author ludangxin
 * @date 2021/8/5
 */
@Data
public class SysUser  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
//    @NotNull(message = "id不能为空", groups = {ValidationInterface.Update.class})
    @NotNull(message = "{id.not.empty}", groups = {ValidationInterface.Update.class})
    private Long id;

    /**
     * 用户名
     */
//    @NotEmpty(message = "用户名称不能为空", groups = {
//            ValidationInterface.Update.class,
//            ValidationInterface.Add.class})
    @NotEmpty(message = "{name.not.empty}", groups = {
            ValidationInterface.Update.class,
            ValidationInterface.Add.class})
    private String username;

    /**
     * 密码
     */
    @Size(min = 6, max = 16, message = "密码长度必须在{min}-{max}之间", groups = {
            ValidationInterface.Update.class,
            ValidationInterface.Add.class})
    private String password = "123456";

    /**
     * 邮箱地址
     */
    @Email(message = "邮箱地址不合法", groups = {
            ValidationInterface.Update.class,
            ValidationInterface.Add.class,
            ValidationInterface.Select.class})
    @NotEmpty(message = "邮箱不能为空", groups = ValidationInterface.Add.class)
    private String email;

    /**
     * 电话
     */
//    @Size(min = 11, max = 11, message = "手机号不合法", groups = {
//            ValidationInterface.Update.class,
//            ValidationInterface.Add.class,
//            ValidationInterface.Select.class})
    @Phone(groups = {
            ValidationInterface.Update.class,
            ValidationInterface.Add.class,
            ValidationInterface.Select.class})
    @NotEmpty(message = "手机号不能为空",groups = {ValidationInterface.Add.class})
    private String phone;
}
