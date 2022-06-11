package cn.poverty.common.alipay;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**

 * @projectName poverty-help-api
 * @Description: 支付宝通用参数
 * @date 2020-04-27
 */
@Data
public class AliPayCommonParam implements Serializable {

    private static final long serialVersionUID = 2606572193429315643L;


    public static void main(String[] args) {
        String json =   "{" +
                "\"out_biz_no\":\"201806300001\"," +
                "\"trans_amount\":23.00," +
                "\"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
                "\"biz_scene\":\"DIRECT_TRANSFER\"," +
                "\"order_title\":\"转账标题\"," +
                "\"original_order_id\":\"20190620110075000006640000063056\"," +
                "\"payee_info\":{" +
                "\"identity\":\"208812*****41234\"," +
                "\"identity_type\":\"ALIPAY_USER_ID\"," +
                "\"name\":\"黄龙国际有限公司\"" +
                "    }," +
                "\"remark\":\"单笔转账\"," +
                "\"business_params\":\"{\\\"sub_biz_scene\\\":\\\"REDPACKET\\\"}\"" +
                "  }";

        HashMap hashMap = JSON.parseObject(json, HashMap.class);

        System.out.println(hashMap);

    }




}
