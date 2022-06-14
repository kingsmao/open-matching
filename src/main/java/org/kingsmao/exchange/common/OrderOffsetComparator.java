package org.kingsmao.exchange.common;

import org.kingsmao.exchange.entity.ExOrder;

import java.util.Comparator;

public class OrderOffsetComparator implements Comparator<ExOrder> {
    @Override
    public int compare(ExOrder o1, ExOrder o2) {
        /*//从mq中拉取
        int result = o1.getOffset().compareTo(o2.getOffset());
        if (result == 0) {
            //从数据库中拉取
            result = o1.getId().compareTo(o2.getId());
        }
        return result;*/

        return  o1.getId().compareTo(o2.getId());
    }
}
