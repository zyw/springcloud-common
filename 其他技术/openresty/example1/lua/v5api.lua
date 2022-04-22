--
-- Created by IntelliJ IDEA.
-- User: AS
-- Date: 2019-09-16
-- Time: 14:47
-- To change this template use File | Settings | File Templates.
--
local http = require("http")
local timer = require("timer")

local _M = {
    _VERSION = "1.0"
}

local function http_get()
    http.get("http://192.168.1.5:9992/json")
end

function _M.req_list()
    timer.http_req(0,http_get,nil)
end

return _M

