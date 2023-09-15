package org.example.top;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.WdkUmsInboundSavePalletRequest;
import com.taobao.api.request.WdkUmsInboundUnbindPalletRequest;
import com.taobao.api.response.WdkUmsInboundSavePalletResponse;
import com.taobao.api.response.WdkUmsInboundUnbindPalletResponse;

/**
 * @author 苍韧
 * @date 2023/4/3
 */
public class TopTest {
    public static void main(String[] args) {
        String url = "https://pre-gw.api.taobao.com/top/router/rest";
        //String url="http://gw.api.taobao.com/router/rest";
        String appkey = "33891032";
        String secret = "a34aae808c3d32b4ccd237f1dc96b17b";
        String warehouseCode = "HT_TEST_B2B06";
        String containerCode = "HD0064267";
        String poNum = "HPTESTNew20230414163217488";
        String itemCode = "281372009";
        String quantity = "10";
        savePallet(url, appkey, secret, warehouseCode, containerCode, poNum, itemCode, quantity);
        //unbindPallet(url, appkey, secret, warehouseCode, containerCode, poNum, itemCode, quantity);
    }

    private static void savePallet(String url,String appkey,String secret,
        String warehouseCode,String containerCode,String poNum,String itemCode,String quantity) {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        WdkUmsInboundSavePalletRequest req = new WdkUmsInboundSavePalletRequest();
        WdkUmsInboundSavePalletRequest.InboundPalletSaveDockerRequest obj1 = new WdkUmsInboundSavePalletRequest.InboundPalletSaveDockerRequest();
        List<WdkUmsInboundSavePalletRequest.InboundPalletDetailDockerRequest> list3 = new ArrayList<WdkUmsInboundSavePalletRequest.InboundPalletDetailDockerRequest>();
        WdkUmsInboundSavePalletRequest.InboundPalletDetailDockerRequest obj4 = new WdkUmsInboundSavePalletRequest.InboundPalletDetailDockerRequest();
        list3.add(obj4);
        obj4.setProduceDate(new Date());
        obj4.setQuantity(quantity);
        obj4.setPalletCode(containerCode);
        obj4.setItemCode(itemCode);
        obj4.setPoNum(poNum);
        obj1.setDetailList(list3);
        obj1.setWarehouseCode(warehouseCode);
        req.setInboundPalletSaveDockerRequest(obj1);
        WdkUmsInboundSavePalletResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println(JSON.toJSONString(rsp));
    }

    private static void unbindPallet(String url,String appkey,String secret,
        String warehouseCode,String containerCode,String poNum,String itemCode,String quantity) {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        WdkUmsInboundUnbindPalletRequest req = new WdkUmsInboundUnbindPalletRequest();
        WdkUmsInboundUnbindPalletRequest.InboundPalletUnbindDockerRequest obj1 = new WdkUmsInboundUnbindPalletRequest.InboundPalletUnbindDockerRequest();
        List<WdkUmsInboundUnbindPalletRequest.InboundPalletDetailDockerRequest> list3 = new ArrayList<WdkUmsInboundUnbindPalletRequest.InboundPalletDetailDockerRequest>();
        WdkUmsInboundUnbindPalletRequest.InboundPalletDetailDockerRequest obj4 = new WdkUmsInboundUnbindPalletRequest.InboundPalletDetailDockerRequest();
        list3.add(obj4);
        obj4.setQuantity(quantity);
        obj4.setPalletCode(containerCode);
        obj4.setItemCode(itemCode);
        obj4.setPoNum(poNum);
        obj1.setDetailList(list3);
        obj1.setWarehouseCode(warehouseCode);
        req.setInboundPalletUnbindDockerRequest(obj1);
        WdkUmsInboundUnbindPalletResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println(JSON.toJSONString(rsp));
    }
}
