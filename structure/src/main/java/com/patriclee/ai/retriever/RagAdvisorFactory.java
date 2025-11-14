package com.patriclee.ai.retriever;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;


import java.util.ArrayList;
import java.util.List;

/**
 * RAG Advisor 工厂类.
 */
@Slf4j
public class RagAdvisorFactory {

    /**
     * RAG 类型常量.
     */
    public static class RagType {
        /** 本地向量检索 */
        public static final String LOCAL = "local";
        /** 云端向量检索 (阿里云百炼) */
        public static final String ALIYUN = "aliyun";
        /** 自定义检索 (预留) */
        public static final String CUSTOM = "custom";
    }

    /**
     * 创建 RAG Advisor 列表.
     * todo 待完善
     * @return Advisor 列表
     */
    public static List<Advisor> createRagAdvisors() {
        List<Advisor> ragAdvisors = new ArrayList<>();
        return ragAdvisors;
    }

    /**
     * 根据RAG类型创建文档检索器.
     * todo 待完善
     */

}
