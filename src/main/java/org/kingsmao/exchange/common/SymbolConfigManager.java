package org.kingsmao.exchange.common;

import com.google.common.collect.Lists;
import match.domain.PairConfig;
import org.kingsmao.exchange.entity.SymbolConfig;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class SymbolConfigManager {

    private static final ConcurrentHashMap<String, SymbolConfig> SYMBOL_CONFIG_MAPPING = new ConcurrentHashMap<>();

    private static List<Integer> robotList = Lists.newArrayList();

    public static SymbolConfig getPairConfig(String symbol) {
        return SYMBOL_CONFIG_MAPPING.get(symbol.toUpperCase());
    }

    public static void setPairConfig(SymbolConfig symbolConfig) {
        SYMBOL_CONFIG_MAPPING.put(symbolConfig.getSymbol(), symbolConfig);
    }

    public static void delPairConfig(String symbol) {
        SYMBOL_CONFIG_MAPPING.entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase(symbol));
    }

    public static ConcurrentHashMap<String, SymbolConfig> getSymbolConfigMapping() {
        return SYMBOL_CONFIG_MAPPING;
    }

    public static List<Integer> getRobotList() {
        return robotList;
    }

    public static void setRobotList(List<Integer> robotList) {
        SymbolConfigManager.robotList = robotList;
    }
}
