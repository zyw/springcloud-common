--
-- Created by IntelliJ IDEA.
-- User: ZYW
-- Date: 2020-04-22
-- Time: 15:23
-- To change this template use File | Settings | File Templates.
--
local videoToM3u8 =  {}

local videoInfoCmd = "%s -v quiet -print_format json -show_format %s";

function videoToM3u8.commonds(cmds)
    local result = os.execute(cmds.cmd1);
    if(result and (cmds.cmd2 ~= nil))then
        result = os.execute(cmds.cmd2);
    end
    return result;
end

function videoToM3u8.videoInfo(args)
    local cmd = string.format(videoInfoCmd,args.cmd,args.filePath)
    local handle = io.popen(cmd)
    local result = handle:read("*a")
    handle:close()
    return result
--    local res,aa,dd = os.execute(cmd);
--    print(aa,"-----------------",dd)
--    return res
end
return videoToM3u8

