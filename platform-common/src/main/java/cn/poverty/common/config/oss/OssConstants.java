package cn.poverty.common.config.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
  * OSS配置参数类
 * 				此处如果是驼峰，需要把baseConstant变为base-constant
  * 
  * @date 2020/12/29
  */
@ConfigurationProperties(prefix = "base-constant.ali-cloud.oss", ignoreUnknownFields = false)
public class OssConstants {

	/**
	 * OSS身份识别 ID
	 */
    private String secretId;

	/**
	 * OSS身份密钥
	 */
    private String secretKey;

	/**
	 * OSS地域信息
	 */
    private String endPoint;

	/**
	 * 存储桶
	 */
	private String bucketName;

	/**
	 * 文件固定前缀
	 */
	private String prefix;

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
