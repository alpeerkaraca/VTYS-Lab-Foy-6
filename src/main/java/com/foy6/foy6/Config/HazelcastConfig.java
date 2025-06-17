package com.foy6.foy6.Config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class HazelcastConfig {
    @Value("${hazelcast.cluster-name}")
    private String clusterName;

    @Value("${hazelcast.network-address}")
    private String networkAddress;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(networkAddress);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
