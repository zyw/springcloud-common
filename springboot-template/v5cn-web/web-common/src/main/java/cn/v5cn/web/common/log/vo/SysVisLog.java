package cn.v5cn.web.common.log.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统访问日志表
 * </p>
 *
 * @author ZYW
 * @since 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysVisLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否执行成功（Y-是，N-否）
     */
    private String success;

    /**
     * 具体消息
     */
    private String message;

    /**
     * ip
     */
    private String ip;

    /**
     * 地址
     */
    private String location;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 操作类型（字典 1登入 2登出）
     */
    private Integer visType;

    /**
     * 访问时间
     */
    private LocalDateTime createTime;

    /**
     * 访问账号
     */
    private String account;


}
