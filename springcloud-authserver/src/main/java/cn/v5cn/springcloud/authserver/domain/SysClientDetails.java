package cn.v5cn.springcloud.authserver.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 配置OAuth2的客户端相关信息表
 * </p>
 *
 * @author ZYW
 * @since 2018-06-01
 */
@TableName("sys_client_details")
public class SysClientDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;
    /**
     * client id`
     */
	@TableField("client_id")
	private String clientId;
    /**
     * secret
     */
	@TableField("client_secret")
	private String clientSecret;
    /**
     * resourceid逗号(,)分割
     */
	@TableField("resource_ids")
	private String resourceIds;
    /**
     * scope
     */
	private String scope;
    /**
     * authorized_grant_types逗号(,)分割
     */
	@TableField("authorized_grant_types")
	private String authorizedGrantTypes;
    /**
     * redirect uri
     */
	@TableField("web_server_redirect_uri")
	private String webServerRedirectUri;
    /**
     * 权限逗号(,)分割
     */
	private String authorities;
	@TableField("access_token_validity")
	private String accessTokenValidity;
	@TableField("refresh_token_validity")
	private String refreshTokenValidity;
	@TableField("additional_information")
	private String additionalInformation;
	private String autoapprove;
    /**
     * 创建时间时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 状态，1 未删除，0 禁用，-1 删除
     */
	private Integer status;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(String accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public String getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(String refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getAutoapprove() {
		return autoapprove;
	}

	public void setAutoapprove(String autoapprove) {
		this.autoapprove = autoapprove;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SysClientDetails{" +
			", id=" + id +
			", clientId=" + clientId +
			", clientSecret=" + clientSecret +
			", resourceIds=" + resourceIds +
			", scope=" + scope +
			", authorizedGrantTypes=" + authorizedGrantTypes +
			", webServerRedirectUri=" + webServerRedirectUri +
			", authorities=" + authorities +
			", accessTokenValidity=" + accessTokenValidity +
			", refreshTokenValidity=" + refreshTokenValidity +
			", additionalInformation=" + additionalInformation +
			", autoapprove=" + autoapprove +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", status=" + status +
			"}";
	}
}
