--
-- Created by IntelliJ IDEA.
-- User: AS
-- Date: 2019-09-16
-- Time: 14:37
-- To change this template use File | Settings | File Templates.
--
local http = require "resty.http"

local _M = {
    _VERSION = "1.0"
}

function _M.get(url,params)
    local httpc = http.new()
    local res, err = httpc:request_uri(url, {
        method = "GET",
        keepalive_timeout = 60,
        keepalive_pool = 10
    })

    if not res then
        ngx.log(ngx.DEBUG,"failed to request: ", err)
        return
    end

    -- In this simple form, there is no manual connection step, so the body is read
    -- all in one go, including any trailers, and the connection closed or keptalive
    -- for you.

    -- ngx.status = res.status

    for k,v in pairs(res.headers) do
        ngx.log(ngx.DEBUG,"header: ",k,v)
    end

    ngx.log(ngx.DEBUG,"body: ",res.body)
end

return _M

