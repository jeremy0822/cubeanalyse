package com.cubead.ca.common.kafka;

import com.cubead.framework.common.ApplicationProperties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Map.Entry;
import java.util.Properties;

/**
 * Created by xiaoao on 5/29/15.
 */
@Component
public class MessageProducer implements Runnable {
    private final static Logger logger = Logger.getLogger(MessageProducer.class);

    private String topic;
    private Properties props;

    private KafkaProducer<Object, Object> producer;

    @PostConstruct
    public void init(){

        String broker = ApplicationProperties.getProproperty("kafka.brokers");
        topic = ApplicationProperties.getProproperty("kafka.topic");
        String groupId = ApplicationProperties.getProproperty("kafka.groupId");
        String zookeepers = ApplicationProperties.getProproperty("kafka.zookeepers");
        String timeout = ApplicationProperties.getProproperty("zookeeper.connection.timeout.ms");
        String acks = ApplicationProperties.getProproperty("request.required.acks");
        String async = ApplicationProperties.getProproperty("producer.type");

        props = new Properties();

        props.put("metadata.broker.list", broker);
        props.put("zookeeper.connect", zookeepers);
        props.put("group.id", groupId);
        props.put("zookeeper.connection.timeout.ms", timeout);
        props.put("request.required.acks", acks);
        props.put("producer.type", async);

        props.put("bootstrap.servers", broker);

        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("client.id", "CAProducer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<Object, Object>(props);
        logAll(props);
        new Thread(this).start();
    }
    
    private void logAll(Properties props) {
    	StringBuilder b = new StringBuilder();
    	b.append("start KafkaProducer ");
        b.append(getClass().getSimpleName());
        b.append(" values: ");
        b.append(Utils.NL);
        for (Entry<Object, Object> entry : props.entrySet()) {
            b.append('\t');
            b.append(entry.getKey());
            b.append(" = ");
            b.append(entry.getValue());
            b.append(Utils.NL);
        }
        logger.info(b.toString());
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void run(){
        try {
            while (true) {
            	final String msg = MessageQueue.getQueue().take();
                long startTime = System.currentTimeMillis();
                producer.send(new ProducerRecord(topic, msg), new CaCallback(startTime, msg));
            }
        } catch (InterruptedException e) {
            logger.error("SEND to Kafka Error: " + e.getMessage());
        }
    }
}
