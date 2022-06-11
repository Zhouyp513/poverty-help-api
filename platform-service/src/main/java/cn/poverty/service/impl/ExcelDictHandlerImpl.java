package cn.poverty.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.poverty.common.constants.BaseConstant;
import cn.poverty.common.constants.PlatformConstant;
import cn.poverty.common.enums.auth.data.DataRoleCodeEnum;
import cn.poverty.common.redis.RedisRepository;
import cn.poverty.common.utils.collection.CollectionUtils;
import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.repository.entity.AuthDepartment;
import cn.poverty.repository.entity.AuthRole;
import cn.poverty.repository.entity.AuthUser;
import cn.poverty.repository.repository.AuthDepartmentRepository;
import cn.poverty.repository.repository.AuthRoleRepository;
import cn.poverty.repository.repository.BusinessDictionaryRepository;
import cn.poverty.repository.result.BusinessDictResult;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author
 * @packageName cn.poverty.service.impl
 * @Description: excel数据导入处理实现类
 * @date 2021-04-22
 */
@Service("excelDictHandler")
@Slf4j
public class ExcelDictHandlerImpl implements IExcelDictHandler {

    @Resource
    private BusinessDictionaryRepository businessDictionaryRepository;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthDepartmentRepository authDepartmentRepository;

    @Resource
    private AuthRoleRepository authRoleRepository;

    /**
      * 数据字典值为key的Map
      */
    ConcurrentHashMap<String, String> dictValueKeyMap = new ConcurrentHashMap<>();

    /**
     * 数据字典键为key的Map
     */
    ConcurrentHashMap<String,String> dictKeyMap = new ConcurrentHashMap<>();

    /**
     * 拿到数据字典集合
     * @author
     * @date 2021/4/22
     * @param dict 字典
     * @return List<Map>
     */
    @Override
    public List<Map> getList(String dict) {
        List<BusinessDictResult> resultList =
                businessDictionaryRepository.queryAllDict(baseConstant.getUnDeleteStatus());
        List<Map> dictMapList = Lists.newArrayList();
        Map<String, List<BusinessDictResult>> dictTypeHashMap = resultList.stream()
                .collect(Collectors.groupingBy(BusinessDictResult::getDictType));
        resultList.stream().forEach(item -> {
            Map map = JSON.parseObject(JSON.toJSONString(item), Map.class);
            dictMapList.add(map);
        });
        log.info(">>>>>>>>>>>>>>>>>查询所有业务字典结果: {} <<<<<<<<<<<<<<<",JSON.toJSONString(dictMapList));
        return dictMapList;
    }


    /**
      * 初始化数据字典
      *
      * @date 2021/4/23
      */
    public void initAllDict() {
        if(!dictKeyMap.isEmpty() || !dictValueKeyMap.isEmpty()){
            return;
        }
        List<BusinessDictResult> resultList = null;
        String result = redisRepository.get(PlatformConstant.ALL_DICT_CACHE_PREFIX);
        if(!CheckParam.isNull(result)){
            resultList = JSON.parseArray(result,BusinessDictResult.class);
        }else{
            resultList =
                    businessDictionaryRepository.queryAllDict(baseConstant.getUnDeleteStatus());
        }
        redisRepository.set(PlatformConstant.ALL_DICT_CACHE_PREFIX,JSON.toJSONString(resultList),1L, TimeUnit.MINUTES);
        resultList.stream().forEach(item -> {
            dictKeyMap.put(item.getBusinessDictionaryId(),item.getDictValue());
            dictValueKeyMap.put(item.getBusinessDictionaryId(),item.getDictValue());
        });
        //初始化部门字典
        Example authDepartmentExample = Example.builder(AuthDepartment.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus()))
                .build();
        List<AuthDepartment> departmentList = authDepartmentRepository.selectByExample(authDepartmentExample);
        if(!CollectionUtils.isEmpty(departmentList)){
            departmentList.stream().forEach(item -> {
                dictKeyMap.put(item.getDepartmentId(),item.getDepartmentName());
                dictValueKeyMap.put(item.getDepartmentName(),item.getDepartmentId());
            });
        }
        //初始化角色数据字典
        Example authRoleExample = Example.builder(AuthRole.class).where(Sqls.custom()
                .andEqualTo("deleteStatus", baseConstant.getUnDeleteStatus())
                .andEqualTo("roleCode", DataRoleCodeEnum.POVERTY_MAN.getCode()))
                .build();
        List<AuthRole> authRoleList = authRoleRepository.selectByExample(authRoleExample);
        if(!CollectionUtils.isEmpty(authRoleList)){
            authRoleList.stream().forEach(item -> {
                dictKeyMap.put(item.getAuthRoleId(),item.getRoleName());
                dictValueKeyMap.put(item.getRoleName(),item.getAuthRoleId());
            });
        }
    }

    /**
      * 字典值转换为名字
      * @author
      * @date 2021/4/22
      * @param dict 字典值
      * @param obj obj
      * @param name name名称
      * @param value 值
      * @return
      */
    @Override
    public String toName(String dict, Object obj, String name, Object value) {
        initAllDict();
        if(CheckParam.isNull(value)){
            return "无";
        }
        String dictValue = dictKeyMap.get(value);
        if(!CheckParam.isNull(dictValue)){
            log.info(">>>>>>>>>>>>>>>>>>>>导入数据的时候定位出来的数据字典: {} <<<<<<<<<<<<<<<<<<<<",dictValue);
            return dictValue;
        }else{
            return "无";
        }
    }


    /**
      * 名字转换为字典值
      * @author
      * @date 2021/4/22
      * @param dict 字典值 此处是导入的值
      * @param obj obj
      * @param name name名称
      * @param value 值
      * @return
      */
    @Override
    public String toValue(String dict, Object obj, String name, Object value) {
        initAllDict();
        if(CheckParam.isNull(value)){
            return null;
        }
        String dictValue = dictValueKeyMap.get(value);
        if(!CheckParam.isNull(dictValue)){
            log.info(">>>>>>>>>>>>>>>>>>>>导入数据的时候定位出来的数据字典: {} <<<<<<<<<<<<<<<<<<<<",dictValue);
            return dictValue;
        }else{
            return "无";
        }
    }
}
