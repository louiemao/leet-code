//package org.example.top;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
//import com.qimencloud.api.DefaultQimenCloudClient;
//import com.qimencloud.api.QimenCloudClient;
//import com.qimencloud.api.sceneqimen.request.WdkUmsInboundOrderGetRequest;
//import com.qimencloud.api.sceneqimen.response.WdkUmsInboundOrderGetResponse;
//import com.taobao.api.ApiException;
//
///**
// * @author 苍韧
// * @date 2023/4/5
// */
//public class QimenTest {
//    //public static void main(String[] args) {
//    //    String qmUrl="https://qimen.api.taobao.com/router/qmtest";
//    //    //String qmUrl="http://qimen.api.taobao.com/router/qm";
//    //    String appKey = "33881841";
//    //    String appSecret = "3d0552c72bf1ee188d60af2f40311f29";
//    //    QimenCloudClient client = new DefaultQimenCloudClient(qmUrl, appKey, appSecret);
//    //    WdkUmsInboundContainerReceivedRequest req = new WdkUmsInboundContainerReceivedRequest();
//    //    req.setTargetAppKey("33891032");
//    //    req.setWarehouseCode("HT_TEST_B2B06");
//    //    List<DetailList> list2=new ArrayList<>();
//    //    WdkUmsInboundContainerReceivedRequest.DetailList obj3
//    //        = new WdkUmsInboundContainerReceivedRequest.DetailList();
//    //    list2.add(obj3);
//    //    obj3.setPalletCode("HX0026924");
//    //    obj3.setPoNum("HPTESTNew20230405110601169");
//    //    obj3.setQuantity("20");
//    //
//    //    req.setDetailList(list2);
//    //    WdkUmsInboundContainerReceivedResponse response = null;
//    //    try {
//    //        response = client.execute(req);
//    //    } catch (ApiException e) {
//    //        throw new RuntimeException(e);
//    //    }
//    //
//    //    System.out.println(JSON.toJSONString(response));
//    //}
//
//    public static void main(String[] args) {
//        String url="https://qimen.api.taobao.com/router/qmtest";
//        //String url="http://qimen.api.taobao.com/router/qm";
//        String appkey = "33881841";
//        String secret = "3d0552c72bf1ee188d60af2f40311f29";
//        QimenCloudClient client = new DefaultQimenCloudClient(url, appkey, secret);
//        WdkUmsInboundOrderGetRequest req = new WdkUmsInboundOrderGetRequest();
//        req.setTargetAppKey("33891032");
//        req.setWarehouseCode("HT_TEST_B2B06");
//        req.setTagCode("DK2508159");
//        WdkUmsInboundOrderGetResponse rsp = null;
//        try {
//            rsp = client.execute(req);
//        } catch (ApiException e) {
//            throw new RuntimeException(e);
//        }
//        JSONObject jsonObject = JSON.parseObject(rsp.getResult().getData());
//        System.out.println(jsonObject.getString("poNum"));
//        System.out.println(jsonObject.getString("itemCode"));
//        System.out.println(jsonObject.getBigDecimal("quantity"));
//        System.out.println(JSON.toJSONString(rsp));
//    }
//}
