package com.marcus.auth.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 基础通用模型通用属性字段
 * @Author Marcus.zheng
 * @Date 2019/8/19 16:42
 **/
@MappedSuperclass
public abstract class BaseModel {

    /**
     * id 采用UUID
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID", length = 20, nullable = false)
    public Long id;

    /** 记录操作人id */
    @Column(name = "operator_id", length = 20)
    protected Long operatorId;

    /** 记录操作人登录账号 */
    @Column(name = "operator_name", length = 30)
    protected String operatorName;

    /** 记录创建时间，不允许修改 */
    @Column(name = "CREATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime = new Date();

    /** 记录最后更新时间 */
    @Column(name = "UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updateTime;

    /** 版本号 */
    @Version
    @Column(name = "OP_VERSION")
    protected Integer opVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

    public Integer getOpVersion() {
        return opVersion;
    }

    public void setOpVersion(Integer opVersion) {
        this.opVersion = opVersion;
    }
}
