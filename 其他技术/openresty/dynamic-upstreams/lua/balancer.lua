local balancer = require("ngx.balancer");
local upstreams = require("upstreams");


local rr_up = upstreams.get_upstreams();
local server = rr_up:find()
ngx.log(ngx.DEBUG,"resty_roundrobin", type(server))
balancer.set_current_peer(server);