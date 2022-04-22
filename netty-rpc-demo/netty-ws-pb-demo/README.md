# 1. 后端
通过protobuf生产java代码
```shell
protoc --experimental_allow_proto3_optional  -I=. --java_out=. student.proto

protoc --proto_path=IMPORT_PATH --java_out=DST_DIR  path/to/file.proto
```

# 2. 前端
## 2.1. Protobuf官网
[Protobuf生成JS](https://developers.google.com/protocol-buffers/docs/reference/javascript-generated)

## 2.2 Protobuf生成js端
### 2.2.1 命令
```shell
protoc --proto_path=proto --js_out=import_style=commonjs,binary:src ack.proto chat.proto
```
* `--proto_path` `.proto`文件所在目录的。
* `--js_out` 生成JS的格式，上面命令是生成`commonjs`格式，也可以是`Closure`格式
* `binary:src` 生成的JS的输出路径，本命令是输出到`src`目录下。
* `ack.proto chat.proto` 是需要处理的`protobuf`文件。

## 2.3. ByteBuffer
[ByteBuffer API](https://www.npmjs.com/package/byte-buffer)