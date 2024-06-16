package org.example.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;

/**
 * @author 苍韧
 * @date 2024/6/4
 */
public class ExcelTxtToJsonUtil {
    public static void main(String[] args) throws IOException {
        String filePathIn = "/Users/maoliang/Downloads/越库配货加价-数据订正-604-613.txt";
        String filePathOut = "/Users/maoliang/Downloads/逸刻越库配货加价-数据订正-604-613-入参.txt";
        //Set<String> decimalTitles = Sets.newHashSet("movingAmount", "sourceQuantity", "targetNoTaxPrice");

        //数据读取
        //List<JSONObject> data = read(filePathIn, decimalTitles);
        List<JSONObject> data = read(filePathIn, null);

        //数据构造
        List<JSONObject> result = build(data);

        //数据写入
        write(filePathOut, result);
    }

    private static void write(String filePathOut, List<JSONObject> result) throws IOException {
        List<List<JSONObject>> partition = Lists.partition(result, 150);
        BufferedWriter out = new BufferedWriter(new FileWriter(filePathOut));
        for (List<JSONObject> list : partition) {
            out.write(JSON.toJSONString(list) + "\n");
        }
        out.close();
    }

    private static List<JSONObject> build(List<JSONObject> data) {
        //按planOrderNo+fu分组
        Map<String, List<JSONObject>> collect = data.stream().collect(
            Collectors.groupingBy(var -> var.getString("配货出库单号") + "_" + var.getString("入库门店编码")));
        List<JSONObject> result = collect.values().stream()
            .map(list -> {
                JSONObject first = list.get(0);
                JSONObject obj = new JSONObject();
                obj.put("isAicInitOrder", false);
                obj.put("bizCode", "PURCHASE_INBOUND_ADJUST");

                JSONObject org = new JSONObject();
                org.put("merchantCode", "YIKE");
                org.put("fu", first.getString("入库门店编码"));
                obj.put("org", org);

                JSONObject orderBO = new JSONObject();
                orderBO.put("orderType", 1);
                orderBO.put("planOrderNo", first.getString("配货出库单号"));
                obj.put("orderBO", orderBO);

                List<JSONObject> adjustAmountSkuRequests = Lists.newArrayList();
                list.stream()
                    .collect(Collectors.groupingBy(var -> var.getString("商品编码")))
                    .forEach((itemCode, itemList) -> {
                        for (int i = 0; i < itemList.size(); i++) {
                            JSONObject var = itemList.get(i);
                            if (i > 0) {
                                System.out.println(var.toJSONString());
                            }
                            JSONObject detail = new JSONObject();
                            detail.put("deptCode", "-1");
                            detail.put("detailOrderId", "-" + (i + 1) + "#" + var.getString("商品编码"));
                            detail.put("itemCode", var.getString("商品编码"));
                            detail.put("movingAmount", var.getBigDecimal(
                                "需调整金额(正数-增加门店成本(即原单少入了成本)，负数-减少门店成本(即原单多入了成本))"));
                            detail.put("sourceOrderNo", var.getString("采购单号"));
                            detail.put("sourceQuantity", var.getBigDecimal("门店实际入库数量"));
                            detail.put("targetNoTaxPrice", var.getBigDecimal("门店入库商品未税单价(正确的)"));
                            adjustAmountSkuRequests.add(detail);
                        }
                    });
                obj.put("adjustAmountSkuRequests", adjustAmountSkuRequests);

                return obj;
            })
            .collect(Collectors.toList());
        return result;
    }

    public static List<JSONObject> read(String filePath, Set<String> decimalTitles) throws IOException {
        // 读取文件
        File file = new File(filePath);
        // 判断文件是否存在
        if (!file.isFile() || !file.exists()) {
            throw new IOException("找不到指定的文件");
        }
        List<JSONObject> data = new ArrayList<>();
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);
            String[] titles = null;
            String str;
            //循环判断是否文件扫描完
            while ((str = bufferedReader.readLine()) != null) {
                if (titles == null) {
                    titles = str.split("\t");
                    continue;
                }
                String[] columns = str.split("\t");
                JSONObject jsonRow = new JSONObject();
                for (int j = 0; j < columns.length; j++) {
                    if (decimalTitles != null && decimalTitles.contains(titles[j])) {
                        jsonRow.put(titles[j], new BigDecimal(columns[j]));
                    } else {
                        jsonRow.put(titles[j], columns[j]);
                    }
                }
                data.add(jsonRow);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return data;
    }

}
