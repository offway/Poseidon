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

    /** 电子刊音频 **/
    private String audioUrl;

    /** 价格 **/
    private Double price;

    /** 阅读数量 **/
    private Long readingNumber;

    /** 销售数量 **/
    private Long soldNumber;

    /** 福利1 **/
    private String welfare1;

    /** 福利2 **/
    private String welfare2;

    /** 福利3 **/
    private String welfare3;

    /** 封面图 **/
    private String coverImageUrl;

    /** 电子刊名称颜色 **/
    private String nameColor;


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

    @Column(name = "reading_number", length = 11)
    public Long getReadingNumber() {
        return readingNumber;
    }

    public void setReadingNumber(Long readingNumber) {
        this.readingNumber = readingNumber;
    }

    @Column(name = "sold_number", length = 11)
    public Long getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(Long soldNumber) {
        this.soldNumber = soldNumber;
    }

    @Column(name = "welfare1", length = 200)
    public String getWelfare1() {
        return welfare1;
    }

    public void setWelfare1(String welfare1) {
        this.welfare1 = welfare1;
    }

    @Column(name = "welfare2", length = 200)
    public String getWelfare2() {
        return welfare2;
    }

    public void setWelfare2(String welfare2) {
        this.welfare2 = welfare2;
    }

    @Column(name = "welfare3", length = 200)
    public String getWelfare3() {
        return welfare3;
    }

    public void setWelfare3(String welfare3) {
        this.welfare3 = welfare3;
    }

    @Column(name = "cover_image_url", length = 200)
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    @Column(name = "name_color", length = 200)
    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }
}
