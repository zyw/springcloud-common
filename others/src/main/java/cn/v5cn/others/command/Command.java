package cn.v5cn.others.command;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Command {
    private static Process command(String command) {
        try {
            String osName = System.getProperty("os.name");
            //System.out.println(osName+"----------------------------");
//            if (osName.contains("Windows")) {
//                return Runtime.getRuntime().exec(new String[]{"cmd", "/c", command});
//            }
//            return Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Process process = Command.command("D:\\software\\ffmpeg-20200113-7225479-win64-static\\bin\\ffmpeg -i D:\\users\\Destop\\1.mp4 -threads 2 -vf scale=1280:-2 -c:v libx264 -preset fast -crf 24 aa.mp4");
        if (process == null) {
            System.out.println("视频转换Process返回为空！");
        }
        boolean wait = process.waitFor(30, TimeUnit.MINUTES);
//        process.waitFor()
//        int result  = process.exitValue();
        System.out.println(" ---------------------------- " + wait);

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        List<String> ins = IOUtils.readLines(br);
        System.out.println("返回数据：" + ins);
        if (ins.isEmpty()) {
            System.out.println("返回数据失败！");
            return;
        }
        ins.forEach(item -> {
            System.out.println(new String(item.getBytes(),StandardCharsets.UTF_8));
        });
    }
}
