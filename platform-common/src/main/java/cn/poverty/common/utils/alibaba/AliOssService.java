package cn.poverty.common.utils.alibaba;

import cn.poverty.common.constants.PlatformConstant;
import cn.poverty.common.exception.BusinessException;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.utils.spring.SnowflakeIdWorker;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.config.oss.ObjectStorage;
import cn.poverty.common.interaction.ImageMeta;
import cn.poverty.common.utils.common.DateTimeUtil;
import cn.poverty.common.utils.collection.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: 阿里OSS文件操作 此处大多操作的是阿里云私有的文件
 * @date 2019-09-18
 */
@Component
@Slf4j
public class AliOssService {

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private ObjectStorage objectStorage;

    @Resource
    private RedisRepository redisRepository;


    /**
     * 查询bucket里面所有的文件
     *
     * @date 2020/12/29
     * @return List
     */
    public List<String> allBucketItems(){
        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }
        ObjectListing objectListing = ossClient.listObjects(baseConstant.getOssBucketName());

        List<OSSObjectSummary> objectSummaryList = objectListing.getObjectSummaries();

        if (!CollectionUtils.isEmpty(objectSummaryList)) {

            ArrayList<String> objectList = Lists.newArrayList();

            objectSummaryList.stream().forEach(b1 -> {
                String key = b1.getKey();
                objectList.add(key);
            });

            return objectList;
        }

        return null;
    }

    /**
     * 查询bucket里面所有的文件
     *
     * @date 2020/12/29
     * @return
     */
    public List<String> allBucketItemsUrl(){

        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }
        ObjectListing objectListing = ossClient.listObjects(baseConstant.getOssBucketName());

        List<OSSObjectSummary> objectSummarieList = objectListing.getObjectSummaries();

        if (!CollectionUtils.isEmpty(objectSummarieList)) {

            ArrayList<String> objectList = Lists.newArrayList();

            objectSummarieList.stream().forEach(b1 -> {

                StringBuffer urlBuffer = new StringBuffer();
                String key = b1.getKey();
                urlBuffer.append(baseConstant.getOssPrefix()).append(key);

                objectList.add(urlBuffer.toString());
            });

            return objectList;
        }

        return null;
    }


    /**
     * 拿到公共的文件的访问路径
     *
     * @date 2019-09-18
     * @param name 文件名称 其实这个就是文件在OSS上面的路径
     * @return String 文件访问的URL路径
     */
    public String getPublicAccessUrl(String name){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        // 创建OSSClient实例。
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。

        if(CheckParam.isNull(name)){
            return "";
        }

        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }

        boolean exist = ossClient.doesObjectExist(baseConstant.getOssBucketName(), name);

        if(!exist){
            return "";
        }else{
            OSSObject object = ossClient.getObject(baseConstant.getOssBucketName(),name);

            // 关闭OSSClient。
            ossClient.shutdown();
            return object.getResponse().getUri();
        }

    }

    /**
     * 拿到远程网络图片信息
     * @param imageUrl
     * @return
     */
    public static BufferedImage remoteBufferedImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            BufferedImage bufferedImage = ImageIO.read(is);
            return bufferedImage;
        } catch (MalformedURLException e) {
            log.error("imageURL: " + imageUrl + ",无效 {} , {}",e.getMessage(),e);
            return null;
        } catch (IOException e) {
            log.error("imageURL: " + imageUrl + ",读取失败 {} , {}",e.getMessage(),e);
            return null;
        }
    }

    /**
     * 将网络图片转成Base64码， 此方法可以解决解码后图片显示不完整的问题
     *
     * @date 2021/2/23
     * @param imgUrl 图片网络地址
     * @param originStatus 是否原始图片(图片名称)
     * @return String
     */
    public String imgBase64(String imgUrl,Boolean originStatus) {

        if(originStatus){
            imgUrl = getPublicAccessUrl(imgUrl);
        }

        String base64RedisImage = redisRepository.get(imgUrl);

        if(!CheckParam.isNull(base64RedisImage)){
            return base64RedisImage;
        }
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        try {
            InputStream inputStream = objectToStream(imgUrl);
            int len = -1;
            while ((len = inputStream.read(data)) != -1) {
                outPut.write(data, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>网络图片转换成成base64格式出现异常, {} , {} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String base64EncodedImage = encoder.encode(outPut.toByteArray());


        StringBuffer imageBuffer = new StringBuffer();
        imageBuffer.append("data:image/jpeg;base64,");
        imageBuffer.append(base64EncodedImage);
        base64EncodedImage = imageBuffer.toString();

        redisRepository.set(imgUrl,base64EncodedImage);
        return base64EncodedImage;
    }


    /**
     * 从URL中读取图片,变成流形式.
     * @param imageUrl 图片URL地址
     * @return InputStream
     */
    public InputStream objectToStream(String imageUrl){
        URL url = null;
        InputStream in = null;
        HttpURLConnection httpUrl = null;
        try{
            url = new URL(imageUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            in = httpUrl.getInputStream();
            return in;
        }catch (Exception e) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>网络图片转换成成base64格式出现异常, {} , {} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
        }
        return null;
    }


    /**
     * 拿到网络图片的宽度和高度
     *
     * @date 2021/2/24
     * @param  imageUrl 图片地址
     * @param originStatus 是否原始图片(图片名称)
     * @return ImageMeta
     */
    public ImageMeta imageMeta(String imageUrl, Boolean originStatus) {

        String imageMetaCache = redisRepository.get(PlatformConstant.IMAGE_META_CACHE_PREFIX + imageUrl);

        if(!CheckParam.isNull(imageMetaCache)){
            return JSON.parseObject(imageMetaCache,ImageMeta.class);
        }

        if(originStatus){
            imageUrl = getPublicAccessUrl(imageUrl);
        }

        BufferedImage image = remoteBufferedImage(imageUrl);

        if (image != null) {
            log.info(">>>>>>>>>>>>>>>BufferedImage的高度: {} <<<<<<<<<<<<" + image.getHeight());
            log.info(">>>>>>>>>>>>>>>BufferedImage的宽度: {} <<<<<<<<<<<<" + image.getWidth());

            ImageMeta imageMeta = new ImageMeta();

            imageMeta.setWidth(image.getWidth());
            imageMeta.setHeight(image.getHeight());

            redisRepository.set(PlatformConstant.IMAGE_META_CACHE_PREFIX + imageUrl,JSON.toJSONString(imageMeta));

            return imageMeta;
        } else {
            log.error("BufferedImage图片不存在!");
            return null;
        }
    }

    /**
     * 向OSS上传文件,返回文件混淆过后的原始名称
     *
     * @date 2019-09-18
     * @param file 文件
     * @param originNameStatus 是否返回原始文件名称
     * @return String
     */
    public String mixFileToOss(MultipartFile file, Boolean originNameStatus){

        StringBuffer fileBuffer = new StringBuffer();

        String originalFilename = file.getOriginalFilename();

        fileBuffer.append(SnowflakeIdWorker.uniqueSequenceStr()).append(originalFilename.substring(originalFilename.lastIndexOf(".")));
        originalFilename = fileBuffer.toString();

        //执行上传文件
        try {
            OSS ossClient = null;
            try {
                ossClient = objectStorage.ossClient();
            } catch (Exception e) {
                throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
            }
            PutObjectResult putObjectResult = ossClient.putObject(baseConstant.getOssBucketName(), originalFilename, file.getInputStream());
            OSSObject object = ossClient.getObject(baseConstant.getOssBucketName(),originalFilename);
            // 关闭OSSClient。
            ossClient.shutdown();

            if(!originNameStatus){
                return object.getResponse().getUri();
            }else{
                return originalFilename;
            }

        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>上传文件的文件流出现异常 : {} <<<<<<<<<<<<<",e.getMessage(),e);
        }

        return originalFilename;
    }

    /**
     * 向OSS上传文件,返回文件的直接链接
     *
     * @date 2019-09-18
     * @param file 文件
     * @return String
     */
    public String multipartFileToOss(MultipartFile file, Boolean mixName){

        StringBuffer fileBuffer = new StringBuffer();

        String originalFilename = file.getOriginalFilename();

        //判断是否混淆
        if(mixName){
            fileBuffer.append(SnowflakeIdWorker.uniqueSequenceStr()).append(originalFilename.substring(originalFilename.lastIndexOf(".")));
        }else{
            fileBuffer.append(originalFilename);
        }

        originalFilename = fileBuffer.toString();

        //执行上传文件
        try {
            OSS ossClient = null;
            try {
                ossClient = objectStorage.ossClient();
            } catch (Exception e) {
                throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
            }
            PutObjectResult putObjectResult = ossClient.putObject(baseConstant.getOssBucketName(), originalFilename, file.getInputStream());
            OSSObject object = ossClient.getObject(baseConstant.getOssBucketName(),originalFilename);
            // 关闭OSSClient。
            ossClient.shutdown();
            return object.getResponse().getUri();
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>上传文件的文件流出现异常 : {} <<<<<<<<<<<<<",e.getMessage(),e);
        }

        return originalFilename;
    }


    /**
     * 向OSS上传文件流
     *
     * @date 2019-09-18
     * @param inputStream 文件流
     * @param fileName 文件名称
     * @return String
     */
    public String streamToOss(InputStream inputStream, String fileName){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践
        // 创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        // 创建OSSClient实例。
        //执行上传文件
        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }
        PutObjectResult putObjectResult = ossClient.putObject(baseConstant.getOssEndPoint(), fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return fileName;
    }



    /**
     * 拿到多个文件的名称
     *
     * @date 2021/2/1
     * @param fileNameList 文件名称集合
     * @return List
     */
    public List<String> getObjectAccessUrlList(List<String> fileNameList){
        List<String> resultList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(fileNameList)) {
            fileNameList.stream().forEach(n1 -> {
                resultList.add((getObjectAccessUrl(n1)));
            });
        }
        return resultList;
    }

    /**
     * 拿到文件的访问路径
     *
     * @date 2019-09-18
     * @param name 文件名称 其实这个就是文件在OSS上面的路径
     * @return String 文件访问的URL路径
     */
    public String getObjectAccessUrl(String name){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        // 创建OSSClient实例。
        // 设置URL过期时间为1小时。
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(Clock.systemDefaultZone().getZone());

        //过期时间
        Date expiration = DateTimeUtil.plusDay(LocalDateTime.now(),1L);

        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }

        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(baseConstant.getOssBucketName(), name, expiration);
        // 关闭OSSClient。
        ossClient.shutdown();
        try {
            return url.toURI().toString();
        } catch (URISyntaxException e) {
            log.error("拿到OSS图片出现错误");
            return null;
        }
    }


    /**
     * 根据文件名称删除OSS的文件
     *
     * @date 2021/2/1
     * @param name 文件名称
     */
    public void deleteObject(String name){
        if(CheckParam.isNull(name)){
            return;
        }
        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }

        ossClient.deleteObject(baseConstant.getOssBucketName(),name);
    }


    /**
     * 删除文件
     *
     * @date 2019-09-18
     * @param name 文件名称 其实这个就是文件在OSS上面的路径
     */
    public void deleteOssObject(String name){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        // 创建OSSClient实例。

        OSS ossClient = null;
        try {
            ossClient = objectStorage.ossClient();
        } catch (Exception e) {
            throw new BusinessException("拿到OSSClient出现异常 , {} , {}",e.getMessage(),e);
        }

        // 删除OSS上面的嗯键
        ossClient.deleteObject(baseConstant.getOssBucketName(), name);
        // 关闭OSSClient。
        ossClient.shutdown();
    }


}
