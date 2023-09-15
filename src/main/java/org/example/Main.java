package org.example;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author 苍韧
 * @date ${DATE}
 */
public class Main {
    public static void main(String[] args) {
        //Set<String> taskCodeSet = new HashSet();
        ////String detailsRelatedTaskCodes=",123";
        ////String[] taskCodes = detailsRelatedTaskCodes.split(",");
        ////taskCodeSet.addAll(Arrays.asList(taskCodes));
        //
        //taskCodeSet.add("123");
        //String join = StringUtils.join(taskCodeSet, ",");

        //String valueString="SCATTERED";
        //List<String> list = Arrays.stream((valueString).split(","))
        //    .filter(StringUtils::isNotBlank)
        //    .map(var -> TypeUtils.castToJavaBean(var.trim(), String.class))
        //    .collect(Collectors.toList());

        //JSONObject jsonObject=new JSONObject();
        //jsonObject.put("value","SCATTERED");
        //String value1 = jsonObject.getObject("value", String.class);
        //String value = JSON.parseObject("SCATTERED", String.class);

        //String str="123|456|789";
        //String[] split = str.split("\\|");
        //System.out.println(JSON.toJSONString(split));

        List<TestA> list = Lists.newArrayList();
        TestA obj = new TestA();
        obj.setShopCode("aaa");
        obj.setTransportRoutePriority(null);
        obj.setTransportRouteWeight(null);
        list.add(obj);

        TestA obj2 = new TestA();
        obj2.setShopCode("bbb");
        obj2.setTransportRoutePriority(1);
        obj2.setTransportRouteWeight(1);
        list.add(obj2);

        list.sort(Comparator.comparing(TestA::getTransportRoutePriority,Comparator.nullsLast(Integer::compareTo))
            .thenComparing(TestA::getTransportRouteWeight,Comparator.nullsLast(Integer::compareTo)));

        System.out.println("Hello world!");
    }

    static class TestA {
        private String shopCode;

        /**
         * 运输路线优先级
         * varchar
         */
        private Integer transportRoutePriority;

        /**
         * 运输路线路顺(路线下站点优先级，决定配送顺序)
         * int
         */
        private Integer transportRouteWeight;

        public String getShopCode() {
            return shopCode;
        }

        public void setShopCode(String shopCode) {
            this.shopCode = shopCode;
        }

        public Integer getTransportRoutePriority() {
            return transportRoutePriority;
        }

        public void setTransportRoutePriority(Integer transportRoutePriority) {
            this.transportRoutePriority = transportRoutePriority;
        }

        public Integer getTransportRouteWeight() {
            return transportRouteWeight;
        }

        public void setTransportRouteWeight(Integer transportRouteWeight) {
            this.transportRouteWeight = transportRouteWeight;
        }
    }
}