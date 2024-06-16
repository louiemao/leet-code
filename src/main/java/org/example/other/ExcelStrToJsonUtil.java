package org.example.other;

import java.math.BigDecimal;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Sets;

/**
 * @author 苍韧
 * @date 2024/6/4
 */
public class ExcelStrToJsonUtil {
    public static void main(String[] args) {
        String excelStr =
            "movingAmount\tdeptCode\tdetailOrderId\titemCode\tsourceOrderNo\tsourceQuantity\ttargetNoTaxPrice\n"
                + "-48.8\t-1\t-1#267876045\t267876045\tHPOYKJQ102240520000064\t80\t7.33\n"
                + "-17.43\t-1\t-1#292528012\t292528012\tHPOYKJQ102240520000065\t15.7\t8.9631\n"
                + "1.93\t-1\t-1#295184007\t295184007\tHPOYKJQ102240520000065\t7\t22.7979\n"
                + "-11.76\t-1\t-1#303928036\t303928036\tHPOYKJQ102240520000064\t28\t2.28\n"
                + "-4.4\t-1\t-1#304274002\t304274002\tHPOYKJQ102240520000064\t20\t3.64\n"
                + "-256.84\t-1\t-1#304356001\t304356001\tHPOYKJQ102240520000064\t64.05\t5.05\n"
                + "6.3\t-1\t-1#304358008\t304358008\tHPOYKJQ102240520000064\t10\t2.04\n"
                + "-1.6\t-1\t-1#304400002\t304400002\tHPOYKJQ102240520000064\t16\t2.96\n"
                + "-6.9\t-1\t-1#304410001\t304410001\tHPOYKJQ102240520000064\t15\t4.54\n"
                + "-5.76\t-1\t-1#304412001\t304412001\tHPOYKJQ102240520000064\t8\t8.5\n"
                + "-56\t-1\t-1#304430005\t304430005\tHPOYKJQ102240520000064\t20\t4\n"
                + "-20.2\t-1\t-1#304692003\t304692003\tHPOYKJQ102240520000064\t10\t6.14\n"
                + "6.35\t-1\t-1#304700009\t304700009\tHPOYKJQ102240520000064\t5\t7.94\n"
                + "0.9\t-1\t-1#304710004\t304710004\tHPOYKJQ102240520000064\t11.25\t3.84\n"
                + "-35.28\t-1\t-1#304726005\t304726005\tHPOYKJQ102240520000064\t19.6\t4\n"
                + "-4.4\t-1\t-1#305844016\t305844016\tHPOYKJQ102240520000064\t10\t2.28\n"
                + "-3.22\t-1\t-1#316538014\t316538014\tHPOYKJQ102240520000064\t7\t4.54\n"
                + "-5.98\t-1\t-1#316618021\t316618021\tHPOYKJQ102240520000064\t23\t5.92\n"
                + "-5.52\t-1\t-1#319064041\t319064041\tHPOYKJQ102240520000064\t4\t9.94\n"
                + "-113.05\t-1\t-1#323172222\t323172222\tHPOYKJQ102240520000065\t77.5\t32.4037\n"
                + "-283.38\t-1\t-1#341894127\t341894127\tHPOYKJQ102240520000065\t85.8\t4.8165\n"
                + "84.18\t-1\t-1#348468060\t348468060\tHPOYKJQ102240520000065\t25\t10.8532\n"
                + "80.59\t-1\t-1#356890010\t356890010\tHPOYKJQ102240520000065\t72\t7.1193";
        Set<String> decimalTitles = Sets.newHashSet("movingAmount", "sourceQuantity", "targetNoTaxPrice");
        System.out.println(convert(excelStr, decimalTitles));
    }

    public static String convert(String excelStr, Set<String> decimalTitles) {
        String[] rows = excelStr.split("\n");
        String[] titles = rows[0].split("\t");

        JSONArray data = new JSONArray();
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i].split("\t");
            JSONObject jsonRow = new JSONObject();
            for (int j = 0; j < row.length; j++) {
                if (decimalTitles != null && decimalTitles.contains(titles[j])) {
                    jsonRow.put(titles[j], new BigDecimal(row[j]));
                } else {
                    jsonRow.put(titles[j], row[j]);
                }
            }
            data.add(jsonRow);
        }
        return data.toJSONString();
    }
}
