package cn.offway.Poseidon.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Entity
@Table(name = "ph_template")
public class PhTemplate implements Serializable {

    /** Id **/
    private Long id;

    /** 杂志名称 **/
    private String templateName;

    /** 状态[0-上架,1-下架] **/
    private String state;

    /** 订阅数量 **/
    private Long subscribeSum;

    /** 页数 **/
    private Long pagesSum;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 电子刊封面 **/
    private String imageUrl;

    /** 电子刊封面 **/
    private String audioUrl;

    /** 价格 **/
    private Double price;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "template_name", length = 100)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Column(name = "state", length = 2)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "subscribe_sum", length = 11)
    public Long getSubscribeSum() {
        return subscribeSum;
    }

    public void setSubscribeSum(Long subscribeSum) {
        this.subscribeSum = subscribeSum;
    }

    @Column(name = "pages_sum", length = 11)
    public Long getPagesSum() {
        return pagesSum;
    }

    public void setPagesSum(Long pagesSum) {
        this.pagesSum = pagesSum;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "image_url", length = 200)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "audio_url", length = 200)
    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Column(name = "price", precision = 15, scale = 2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
