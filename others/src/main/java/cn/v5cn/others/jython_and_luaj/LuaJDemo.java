package cn.v5cn.others.jython_and_luaj;

import cn.hutool.json.*;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.*;

import java.net.URISyntaxException;

/**
 * 使用Lua执行shell命令，完成视频压缩和分片。
 * @author ZYW
 */
public class LuaJDemo {

    public static void main(String[] args) throws URISyntaxException {

        String path = LuaJDemo.class.getClassLoader().getResource("videoToM3u8.lua").toURI().getPath();

        Globals globals = JsePlatform.standardGlobals();
        LuaValue chunk = globals.loadfile(path).call();

        LuaValue func = chunk.get(LuaValue.valueOf("videoInfo"));

        LuaValue params = new LuaTable();
        params.set("cmd","D:\\software\\ffmpeg-20200113-7225479-win64-static\\bin\\ffprobe");
        params.set("filePath","D:\\users\\Destop\\20200420154536.mp4");

        String result = func.call(params).toString();
//        JSONObject jsonObject = JSONUtil.parseObj(result);
//        Object duration = jsonObject.getJSONObject("format").get("duration");

        System.out.println(result+"--------------" + 0);
    }
}