--
-- Created by IntelliJ IDEA.
-- User: AS
-- Date: 2019-09-16
-- Time: 14:18
-- To change this template use File | Settings | File Templates.
--
local ngx_timer_at = ngx.timer.at

local _M = {
    _VERSION="1.0"
}

function _M.http_req(delay,callback,obj)
    local ok,err = ngx_timer_at(delay,callback,obj);
    if not ok then
        ngx.log(ngx.ERR,"failed to create timer: ",err)
    end
end

return _M

