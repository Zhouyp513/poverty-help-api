package cn.poverty.repository.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

/**
 
 * @Title: ContractSQLProvider
 * @ProjectName poverty-help-api
 * @Description: 合同统计SQL
 * @date 2019/1/8 18:18
 */
@Slf4j
public class ContractSqlProvider {

    /**
     * 根据合同ID查询合同详情
     
     * @date 2019/1/11 16:40
     * @param contractId 合同ID
     * @return java.lang.String
     */
    public String queryContractInfoByContractId(@Param("contractId") Integer contractId){
        SQL sql = new SQL();

        sql.SELECT(" c.* ,r.ridgepole_id as ridgepoleId,r.room_number as roomNumber,AES_DECRYPT(t.phone, '52f3F6A18Fe0F6A76e0a2b9a564dE9Da') as tenantPhone," +
                "t.name as tenantName,AES_DECRYPT(t.idcard, '52f3F6A18Fe0F6A76e0a2b9a564dE9Da') as  certificateNumber," +
                "t.gender as gender ,a.apartment_id as apartmentId,a.apartment_name as apartmentName," +
                "t.idcard_type as certificateType ");

        contractInfoByContractIdQueryConditionBuilder(sql,contractId);
        String querySql = new StringBuffer().append(sql.toString()).append("  group by c.contract_id  ").toString();
        log.info(">>>>>>>>>>>>>合同统计SQL<<<<<<<<<<<<"+ querySql);
        return querySql;
    }

    /**
     *
     * 合同详情信息条件查询构建器
     
     * @date 2019/1/11 16:38
     * @param sql SQL条件
     * @param contractId 合同ID
     * @return
     */
    public void contractInfoByContractIdQueryConditionBuilder(SQL sql,Integer contractId){
        sql.FROM(" contract as c ");
        sql.LEFT_OUTER_JOIN(" room as r on ( c.`room_id`= r.`room_id` ) ");
        sql.LEFT_OUTER_JOIN(" `tenant_contract_relationship` as tcr on(c.`contract_id` = tcr.`contract_id`) ");
        sql.LEFT_OUTER_JOIN(" `tenant` as t on(tcr.`tenant_id` = t.`tenant_id`) ");
        sql.LEFT_OUTER_JOIN(" `apartment` as a on(a.`apartment_id` = r.`apartment_id`) ");
        sql.WHERE(" c.contract_id =   " + contractId);
        sql.WHERE(" 1 = 1");
    }
}
