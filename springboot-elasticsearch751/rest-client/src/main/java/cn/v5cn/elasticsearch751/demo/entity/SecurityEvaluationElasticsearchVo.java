package cn.v5cn.elasticsearch751.demo.entity;

import java.io.Serializable;

public class SecurityEvaluationElasticsearchVo implements Serializable {

    /**
     * 资源ID
     */
    private Long id;
    /**
     * 1:CPE课程，2：知识共享，3：等保圈问答
     */
    private Integer type;

    /**
     * 如果type是1，title就是CPE课程名称
     *          2，知识恭喜标题
     *          3，问题标题
     */
    private String title;

    /**
     * 如果type是1，name就是CPE课程讲师名
     *          2，发知识的人名
     *          3，发问题的人名
     */
    private String name;
    /**
     * 如果type是1，content就是CPE课程简介
     *          2，发知识的内容
     *          3，发问题的内容
     */
    private String content;
    /**
     * 如果type是1，fileId就是CPE课程讲师头像
     *          2，发知识的人头像
     *          3，发问题的人头像
     */
    private String fileId;

    /**
     * 资源创建时间
     */
    private String createTime;

    /**
     * 课程状态，只在type=1时有用
     */
    private String status;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
