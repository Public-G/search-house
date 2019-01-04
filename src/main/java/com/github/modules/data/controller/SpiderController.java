package com.github.modules.data.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.annotation.SysLog;
import com.github.common.constant.SysConstant;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.CustomResponseWrapper;
import com.github.common.utils.MapUtils;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.constant.CommunicateConstant;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SettingEntity;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.data.pojo.SpiderCluster;
import com.github.modules.data.service.RuleService;
import com.github.modules.data.service.SettingService;
import com.github.modules.data.service.SpiderService;
import com.google.common.collect.Lists;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.config.NamespaceUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 爬虫项目管理
 *
 * @author ZEALER
 * @date 2018-11-12
 */
@RequestMapping("/data/spider")
@Controller
public class SpiderController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 列表/添加 页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType, Model model) {
        if ("Add".equals(forwardType)) {
            reference(model);
        }

        return "admin/data/spider/spider" + forwardType;
    }

    /**
     * 爬虫项目列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm) {
        PageUtils pageBean = spiderService.findPage(pageForm);

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 保存爬虫项目
     */
    @SysLog("保存爬虫项目")
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SpiderEntity spiderEntity) {
        ValidatorUtils.validateEntity(spiderEntity);

        spiderService.save(spiderEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String spiderName) {
        SpiderEntity spiderEntity = spiderService.findByName(spiderName);
        if (spiderEntity == null) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail("项目名已存在");
    }

    /**
     * 爬虫项目信息
     */
    @GetMapping("/info/{spiderId:[0-9]+}")
    public String info(@PathVariable Long spiderId, Model model) {
        SpiderEntity spider = spiderService.findById(spiderId);
        model.addAttribute("spider", spider);

        reference(model);

        return "admin/data/spider/spiderInfo";
    }

    /**
     * 修改爬虫项目
     */
    @SysLog("修改爬虫项目")
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("spider") SpiderEntity spiderEntity) {
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (spiderEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(spiderEntity);

        spiderService.update(spiderEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除爬虫项目
     */
    @SysLog("删除爬虫项目")
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] spiderIds) {
        spiderService.deleteBatch(spiderIds);

        return ApiResponse.ofSuccess();
    }

    /**
     * 将渲染后的html返回
     */
    private String getHtmlOutput(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomResponseWrapper wrapper = new CustomResponseWrapper(response);
        request.getRequestDispatcher("/template/spiderUploadList").include(request, wrapper);
        return wrapper.getContent();
    }

    /**
     * 页面传值
     */
    private void reference(Model model) {
        List<RuleEntity> allRule = ruleService.findAll();
        List<SettingEntity> allSetting = settingService.findAll();
        model.addAttribute("rule", allRule);
        model.addAttribute("setting", allSetting);
    }

    /**
     * 跳转部署页面
     */
    @GetMapping("/upload/{spiderId:[0-9]+}")
    public String upload(@PathVariable Long spiderId, Model model) {
        model.addAttribute("spiderId", spiderId);
        return "admin/data/spider/spiderUpload";
    }

    /**
     * 获取爬虫集群信息
     */
    @GetMapping("/cluster")
    @ResponseBody
    public ApiResponse cluster() throws IOException {
        // 发送心跳请求
        Object receive = rabbitTemplate.convertSendAndReceive(CommunicateConstant.RABBITMQ_COMMAND_QUEUE,
                new MapUtils().put("code", CommunicateConstant.CommandType.HEARTBEAT.getCode()));

        Map response = responseConvert(receive);
        if (response == null) {
            return ApiResponse.ofFail("未知错误，请检查集群")
                    .put("pageBean", new PageUtils(Lists.newArrayList()));
        }

        // 检查是否返回错误状态码
        if (Objects.equals(SysConstant.FAIL_CODE, response.get("code"))) {
            return ApiResponse.ofFail(String.valueOf(response.get("msg")))
                    .put("pageBean", new PageUtils(Lists.newArrayList()));
        }

        String clusterContent = objectMapper.writeValueAsString(response);
        SpiderCluster spiderCluster = objectMapper.readValue(clusterContent, SpiderCluster.class);

        PageUtils pageBean = new PageUtils(Lists.newArrayList(spiderCluster));

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 部署项目(开始爬虫)
     */
    @SysLog("部署爬虫项目")
    @PostMapping("/deploy")
    @ResponseBody
    public ApiResponse deploy(@RequestParam Long spiderId) {
        // 参数检验不通过返回400
        if (spiderId == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.BAD_REQUEST);
        }

        checkCluster(CommunicateConstant.ClusterStatus.RUNNING.getCode());

        // RPC调用
        Object receive = rabbitTemplate.convertSendAndReceive(CommunicateConstant.RABBITMQ_COMMAND_QUEUE,
                new MapUtils().put("code", CommunicateConstant.CommandType.CRAWL.getCode())
                        .put("data", spiderId));

        ApiResponse processResult = rpcProcessor(receive);

        if (Objects.equals(SysConstant.SUCCESS_CODE, processResult.get("code"))) {
            SpiderEntity spiderEntity = spiderService.findById(spiderId);

            if (StringUtils.isBlank(spiderEntity.getStartUrls())) {
                return ApiResponse.ofFail("起始链接存入redis失败，请检查起始链接");
            }
            // 存入起始链接
            stringRedisTemplate.opsForList().leftPush(CommunicateConstant.START_URLS_KEY, spiderEntity.getStartUrls());
        }

        return processResult;
    }

    /**
     * 暂停爬虫
     */
    @SysLog("暂停爬虫")
    @PostMapping("/paused")
    @ResponseBody
    public ApiResponse paused() {
        // 检查集群状态是否满足条件
        checkCluster(CommunicateConstant.ClusterStatus.CRAWL.getCode());

        // RPC调用
        Object receive = rabbitTemplate.convertSendAndReceive(CommunicateConstant.RABBITMQ_COMMAND_QUEUE,
                new MapUtils().put("code", CommunicateConstant.CommandType.PAUSED.getCode()));

        return rpcProcessor(receive);
    }

    /**
     * 恢复爬虫
     */
    @SysLog("恢复爬虫")
    @PostMapping("/resumed")
    @ResponseBody
    public ApiResponse resumed() {
        // 检查集群状态是否满足条件
        checkCluster(CommunicateConstant.ClusterStatus.PAUSED.getCode());

        // RPC调用
        Object receive = rabbitTemplate.convertSendAndReceive(CommunicateConstant.RABBITMQ_COMMAND_QUEUE,
                new MapUtils().put("code", CommunicateConstant.CommandType.RESUMED.getCode()));

        return rpcProcessor(receive);
    }

    /**
     * 处理业务请求返回
     * @param receive rpc返回值
     * @return
     */
    private ApiResponse rpcProcessor(Object receive) {
        Map response = responseConvert(receive);
        if (response == null) {
            return ApiResponse.ofFail("未知错误，请检查集群");
        }

        int code = (Integer) response.get("code");
        if (Objects.equals(SysConstant.SUCCESS_CODE, code)) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail(String.valueOf(response.get("msg")));

    }

    /**
     * 检查集群状态
     * @param status 集群状态码
     */
    private void checkCluster(int status) {
        // 发送心跳请求
        Object receive = rabbitTemplate.convertSendAndReceive(CommunicateConstant.RABBITMQ_COMMAND_QUEUE,
                new MapUtils().put("code", CommunicateConstant.CommandType.HEARTBEAT.getCode()));

        Map response = responseConvert(receive);
        if (response == null) {
            throw new SHException("未知错误，请检查集群");
        } else {
            int code = (Integer) response.get("code");
            if (Objects.equals(SysConstant.SUCCESS_CODE, code)) {
                // 不符合传入的状态信息则抛出异常
                if (!Objects.equals(status, response.get("status"))) {
                    throw new SHException("操作无效");
                }
            } else {
                throw new SHException(String.valueOf(response.get("msg")));
            }
        }
    }

    /**
     * python应用程序返回格式转换
     * @param receive rpc返回值
     * @return
     */
    private Map responseConvert(Object receive) {
        if (null != receive && receive instanceof Map) {
            return (Map) receive;
        }

        return null;
    }

    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "spiderId", required = false) Long spiderId, Model model) {
        if (spiderId != null) {
            model.addAttribute("spider", spiderService.findById(spiderId));
        }
    }
}
