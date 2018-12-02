package com.github.modules.data.constant;

/**
 * 与爬虫集群通信常量
 *
 * @author ZEALER
 * @date 2018-11-29
 */
public class CommunicateConstant {

    /**
     * redis中存放爬虫起始链接KEY
     */
    public static final String START_URLS_KEY = "house:start_urls";

    /**
     * RabbitMQ处理数据的Exchange名
     */
    public static final String RABBITMQ_HOUSE_EXCHANGE = "house_exchange";

    /**
     * RabbitMQ处理数据的Queue名
     */
    public static final String RABBITMQ_HOUSE_QUEUE = "house_queue";

    /**
     * RabbitMQ处理通信的Exchange名
     */
    public static final String RABBITMQ_COMMAND_EXCHANGE = "command_exchange";

    /**
     * RabbitMQ处理通信的Queue名
     */
    public static final String RABBITMQ_COMMAND_QUEUE = "command_queue";

    /**
     * 与集群通信类型
     *
     * @author ZEALER
     * @date 2018-11-29
     */
    public enum CommandType {

        /**
         * 暂停爬取
         */
        PAUSED(1),

        /**
         * 恢复爬取
         */
        RESUMED(2),

        /**
         * 开始爬取
         */
        CRAWL(6),

        /**
         * 心跳
         */
        HEARTBEAT(3000);

        private int code;

        CommandType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


    /**
     * 集群返回状态
     *
     * @author ZEALER
     * @date 2018-11-29
     */
    public enum ClusterStatus {
        RUNNING(0, "正常"),

        PAUSED(1, "爬虫暂停"),

//        RESUMED(2, "爬虫运行"),

        CRAWL(6, "爬虫运行");

        private int code;

        private String message;

        ClusterStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static String ofMessage(int code) {
            for (ClusterStatus cluster : ClusterStatus.values()) {
                if (cluster.getCode() == code) {
                    return cluster.getMessage();
                }
            }
            return "";
        }


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
