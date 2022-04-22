package cn.v5cn.others.gid;

import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> ids = Lists.newArrayList();
        for (int i = 1; i <= 100; i++) {
            String idStr = IdWorker.getIdStr();
            ids.add(idStr);
            System.out.println("INSERT INTO `cps_school_year` VALUES (" + idStr + ", " + (2020 + i) + ", 40.00, 40.00, 1, NOW(), NOW());");
        }

        ids.forEach(id -> {
            System.out.println("delete from `cps_school_year` where id ='" + id + "';");
        });
    }
}
