local http = require("socket.http")
local ltn12 = require("ltn12")
local cjson = require("cjson.safe")
local resty_roundrobin = require("resty.roundrobin");

local _M = {
    _VERSION="1.0"
}

function _M:update_upstreams()
    -- 从redis中拉去
    -- 动态更新upstream
    local resp = {}

   local res, code, response_headers =  http.request{
        url = "http://192.168.1.5:9992/json", sink = ltn12.sink.table(resp)
    }
    ngx.log(ngx.DEBUG,"result: ",res);
    ngx.log(ngx.DEBUG,"code: ",code);

    if code == 200 then
        local resp,err = cjson.decode(resp[1]);
        if not resp then
            ngx.log(ngx.ERR,"table to json error: ", err)
            return
        end
        local upstreams = {}
        for _, v in ipairs(resp) do
            local ip_port = v.Address .. ":" .. v.ServicePort;
            upstreams[ip_port] = 1;
        end
        local ups_list_json = cjson.encode(upstreams);
        ngx.log(ngx.DEBUG,"upstream list json", ups_list_json);
        ngx.shared.upstream_list:set("backends", ups_list_json)
    else
        ngx.log(ngx.ERR,"msg: 请求服务列表接口异常！")
    end
end

function _M:get_upstreams()
   local upstreams_str = ngx.shared.upstream_list:get("backends")
   ngx.log(ngx.DEBUG,"shared get ups json: ",upstreams_str)
   local upstreams, err = cjson.decode(upstreams_str)
   ngx.log(ngx.DEBUG,"shared get ups json---------------: ",type(upstreams))
   if not upstreams then
        ngx.log(ngx.ERR,"get shared ups string json to table!", err)
   end
   return resty_roundrobin:new(upstreams)
end

return _M
