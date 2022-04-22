OpenResty学习记录
## 安装luasocket
### Linux(CentOS7)
#### 第一步 
[下载地址](http://files.luaforge.net/releases/luasocket/luasocket/luasocket-2.0.2) 下载`luasocket-2.0.2.tar.gz`版本
#### 第二步
解压并修改config文件
```properties
#------
# LuaSocket makefile configuration
#

#------
# Output file names
#
EXT=so
SOCKET_V=2.0.2
MIME_V=1.0.2
SOCKET_SO=socket.$(EXT).$(SOCKET_V) 
MIME_SO=mime.$(EXT).$(MIME_V)
UNIX_SO=unix.$(EXT)

#------
# Lua includes and libraries
#
#LUAINC=-I/usr/local/include/lua50
#LUAINC=-I/usr/local/include/lua5.1
# 修改成自己安装openresty的地址其中包含lua.h文件
LUAINC=-I/usr/local/openresty/luajit/include/luajit-2.1

#------
# Compat-5.1 directory
#
#COMPAT=compat-5.1r5

#------
# Top of your Lua installation
# Relative paths will be inside the src tree
#
#INSTALL_TOP_SHARE=/usr/local/share/lua/5.0
#INSTALL_TOP_LIB=/usr/local/lib/lua/5.0
# 输出编译后的文件包括*.lua文件
INSTALL_TOP_SHARE=/usr/local/openresty/luajit/share/lua/5.1
# 输出core.so文件的路径
INSTALL_TOP_LIB=/usr/local/openresty/luajit/lib/lua/5.1

INSTALL_DATA=cp
INSTALL_EXEC=cp

#------
# Compiler and linker settings
# for Mac OS X
#
#CC=gcc
#DEF= -DLUASOCKET_DEBUG -DUNIX_HAS_SUN_LEN
#CFLAGS= $(LUAINC) -I$(COMPAT) $(DEF) -pedantic -Wall -O2 -fno-common
#LDFLAGS=-bundle -undefined dynamic_lookup
#LD=export MACOSX_DEPLOYMENT_TARGET="10.3"; gcc

#------
# Compiler and linker settings
# for Linux
CC=gcc
DEF=-DLUASOCKET_DEBUG 
CFLAGS= $(LUAINC) $(DEF) -pedantic -Wall -O2 -fpic
LDFLAGS=-O -shared -fpic
LD=gcc 

#------
# End of makefile configuration
#
```
#### 第三步
修改`luasocket-2.0.2/src`路径中所有`luaL_reg`为`luaL_Reg`，如果没有修改执行运行`sudo make`
也会提示是否要修改。
#### 第四步
```shell script
sudo make && make install
```
就安装完成了
### Mac OSX安装
参见https://cloud.tencent.com/developer/article/1011303网站方法安装