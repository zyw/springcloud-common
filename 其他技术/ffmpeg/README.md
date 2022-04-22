## 视频mp4转码到m3u8
```shell script
ffmpeg -i output.mp4 -codec copy -vbsf h264_mp4toannexb -map 0 -f segment -segment_list output.m3u8 -segment_time 10 out%03d.ts
```
其中`output.mp4`是需要转码的文件，`output.m3u8`是最后生成的`m3u8`文本文件的名称，`-segment_time 10`是10秒分一片，`out%03d.ts`是最后生成的ts格式的视频分片。

## 视频截图
```shell script
./ffmpeg -i  1.mp4 -f image2 -an -ss 0:0:0.0 -an -r 1 -vframes 1 -y -s 600x450 4.jpg
```
其中`1.mp4`是需要截图的视频文件，`-ss 0:0:0.0`是需要截图的位置（秒数），`-s 600x450`截图大小，`4.jpg`是截图图片文件的名称。

## 视频压缩
```shell script
ffmpeg -i input.mp4 -vf scale=1280:-1 -c:v libx264 -preset veryslow -crf 24 output.mp4
```
请注意，如果你尝试减小视频文件的大小，你将损失视频质量。如果 24 太有侵略性，你可以降低 -crf 值到或更低值。
<br/>
<br/>

> 以下内容出自：[github tonydeng](https://github.com/tonydeng/fmj/blob/master/ffmpeg.md)
# FFMPEG 使用说明

- [FFMPEG 使用说明](#ffmpeg-%e4%bd%bf%e7%94%a8%e8%af%b4%e6%98%8e)
  - [截图命令](#%e6%88%aa%e5%9b%be%e5%91%bd%e4%bb%a4)
    - [截取一张352x240尺寸大小，格式为jpg的图片](#%e6%88%aa%e5%8f%96%e4%b8%80%e5%bc%a0352x240%e5%b0%ba%e5%af%b8%e5%a4%a7%e5%b0%8f%e6%a0%bc%e5%bc%8f%e4%b8%bajpg%e7%9a%84%e5%9b%be%e7%89%87)
    - [把视频的前30帧转换成一个Animated Gif](#%e6%8a%8a%e8%a7%86%e9%a2%91%e7%9a%84%e5%89%8d30%e5%b8%a7%e8%bd%ac%e6%8d%a2%e6%88%90%e4%b8%80%e4%b8%aaanimated-gif)
    - [在视频的第8.01秒出截取230x240的缩略图](#%e5%9c%a8%e8%a7%86%e9%a2%91%e7%9a%84%e7%ac%ac801%e7%a7%92%e5%87%ba%e6%88%aa%e5%8f%96230x240%e7%9a%84%e7%bc%a9%e7%95%a5%e5%9b%be)
    - [每隔一秒截一张图](#%e6%af%8f%e9%9a%94%e4%b8%80%e7%a7%92%e6%88%aa%e4%b8%80%e5%bc%a0%e5%9b%be)
    - [每隔20秒截一张图](#%e6%af%8f%e9%9a%9420%e7%a7%92%e6%88%aa%e4%b8%80%e5%bc%a0%e5%9b%be)
    - [多张截图合并到一个文件里（2x3）每隔一千帧(秒数=1000/fps25)即40s截一张图](#%e5%a4%9a%e5%bc%a0%e6%88%aa%e5%9b%be%e5%90%88%e5%b9%b6%e5%88%b0%e4%b8%80%e4%b8%aa%e6%96%87%e4%bb%b6%e9%87%8c2x3%e6%af%8f%e9%9a%94%e4%b8%80%e5%8d%83%e5%b8%a7%e7%a7%92%e6%95%b01000fps25%e5%8d%b340s%e6%88%aa%e4%b8%80%e5%bc%a0%e5%9b%be)
    - [从视频中生成GIF图片](#%e4%bb%8e%e8%a7%86%e9%a2%91%e4%b8%ad%e7%94%9f%e6%88%90gif%e5%9b%be%e7%89%87)
    - [转换视频为图片（每帧一张图）](#%e8%bd%ac%e6%8d%a2%e8%a7%86%e9%a2%91%e4%b8%ba%e5%9b%be%e7%89%87%e6%af%8f%e5%b8%a7%e4%b8%80%e5%bc%a0%e5%9b%be)
    - [图片转换为视频](#%e5%9b%be%e7%89%87%e8%bd%ac%e6%8d%a2%e4%b8%ba%e8%a7%86%e9%a2%91)
  - [切分视频并生成M3U8文件](#%e5%88%87%e5%88%86%e8%a7%86%e9%a2%91%e5%b9%b6%e7%94%9f%e6%88%90m3u8%e6%96%87%e4%bb%b6)
  - [分离视频音频流](#%e5%88%86%e7%a6%bb%e8%a7%86%e9%a2%91%e9%9f%b3%e9%a2%91%e6%b5%81)
  - [视频解复用](#%e8%a7%86%e9%a2%91%e8%a7%a3%e5%a4%8d%e7%94%a8)
  - [视频转码](#%e8%a7%86%e9%a2%91%e8%bd%ac%e7%a0%81)
  - [视频封装](#%e8%a7%86%e9%a2%91%e5%b0%81%e8%a3%85)
  - [视频剪切](#%e8%a7%86%e9%a2%91%e5%89%aa%e5%88%87)
  - [视频录制](#%e8%a7%86%e9%a2%91%e5%bd%95%e5%88%b6)
  - [YUV序列播放](#yuv%e5%ba%8f%e5%88%97%e6%92%ad%e6%94%be)
  - [YUV序列转AVI](#yuv%e5%ba%8f%e5%88%97%e8%bd%acavi)
    - [常用参数说明](#%e5%b8%b8%e7%94%a8%e5%8f%82%e6%95%b0%e8%af%b4%e6%98%8e)
      - [主要参数](#%e4%b8%bb%e8%a6%81%e5%8f%82%e6%95%b0)
      - [视频参数](#%e8%a7%86%e9%a2%91%e5%8f%82%e6%95%b0)
    - [音频参数](#%e9%9f%b3%e9%a2%91%e5%8f%82%e6%95%b0)
  - [使用ffmpeg合并MP4文件](#%e4%bd%bf%e7%94%a8ffmpeg%e5%90%88%e5%b9%b6mp4%e6%96%87%e4%bb%b6)
  - [使用ffmpeg转换flv到mp4](#%e4%bd%bf%e7%94%a8ffmpeg%e8%bd%ac%e6%8d%a2flv%e5%88%b0mp4)
  - [视频添加水印](#%e8%a7%86%e9%a2%91%e6%b7%bb%e5%8a%a0%e6%b0%b4%e5%8d%b0)
    - [水印局中](#%e6%b0%b4%e5%8d%b0%e5%b1%80%e4%b8%ad)
  - [视频翻转和旋转](#%e8%a7%86%e9%a2%91%e7%bf%bb%e8%bd%ac%e5%92%8c%e6%97%8b%e8%bd%ac)
    - [翻转](#%e7%bf%bb%e8%bd%ac)
      - [水平翻转语法: -vf hflip](#%e6%b0%b4%e5%b9%b3%e7%bf%bb%e8%bd%ac%e8%af%ad%e6%b3%95--vf-hflip)
      - [垂直翻转语法：-vf vflip](#%e5%9e%82%e7%9b%b4%e7%bf%bb%e8%bd%ac%e8%af%ad%e6%b3%95-vf-vflip)
    - [旋转](#%e6%97%8b%e8%bd%ac)
    - [将视频顺时针旋转90度](#%e5%b0%86%e8%a7%86%e9%a2%91%e9%a1%ba%e6%97%b6%e9%92%88%e6%97%8b%e8%bd%ac90%e5%ba%a6)
    - [将视频水平翻转(左右翻转)](#%e5%b0%86%e8%a7%86%e9%a2%91%e6%b0%b4%e5%b9%b3%e7%bf%bb%e8%bd%ac%e5%b7%a6%e5%8f%b3%e7%bf%bb%e8%bd%ac)
    - [顺时针旋转90度并水平翻转](#%e9%a1%ba%e6%97%b6%e9%92%88%e6%97%8b%e8%bd%ac90%e5%ba%a6%e5%b9%b6%e6%b0%b4%e5%b9%b3%e7%bf%bb%e8%bd%ac)
  - [添加字幕](#%e6%b7%bb%e5%8a%a0%e5%ad%97%e5%b9%95)
  - [嵌入字幕](#%e5%b5%8c%e5%85%a5%e5%ad%97%e5%b9%95)

## 截图命令

### 截取一张352x240尺寸大小，格式为jpg的图片

```bash
ffmpeg -i input_file -y -f image2 -t 0.001 -s 352x240 output.jpg
```

### 把视频的前30帧转换成一个Animated Gif

```bash
ffmpeg -i input_file -vframes 30 -y -f gif output.gif
```

### 在视频的第8.01秒出截取230x240的缩略图

```bash
ffmpeg -i input_file -y -f mjpeg -ss 8 -t 0.001 -s 320x240 output.jpg
```

### 每隔一秒截一张图

```bash
ffmpeg -i out.mp4 -f image2 -vf fps=fps=1 out%d.png
```

### 每隔20秒截一张图

```bash
ffmpeg -i out.mp4 -f image2 -vf fps=fps=1/20 out%d.png
```

### 多张截图合并到一个文件里（2x3）每隔一千帧(秒数=1000/fps25)即40s截一张图

```
ffmpeg -i out.mp4 -frames 3 -vf "select=not(mod(n\,1000)),scale=320:240,tile=2x3" out.png
```

### 从视频中生成GIF图片

```bash
ffmpeg -i out.mp4 -t 10 -pix_fmt rgb24 out.gif
```

### 转换视频为图片（每帧一张图）

```bash
ffmpeg -i out.mp4 out%4d.png
```

### 图片转换为视频

```bash
ffmpeg -f image2 -i out%4d.png -r 25 video.mp4
```

## 切分视频并生成M3U8文件

```bash
ffmpeg -i input.mp4 -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 20 -hls_list_size 0 -hls_wrap 0 output.m3u8
```

相关参数说明：

```bash
-i 输入视频文件
-c:v 输出视频格式
-c:a 输出音频格式
-strict
-f hls 输出视频为HTTP Live Stream（M3U8）
-hls_time 设置每片的长度，默认为2，单位为秒
-hls_list_size 设置播放列表保存的最多条目，设置为0会保存所有信息，默认为5
-hls_wrap 设置多少片之后开始覆盖，如果设置为0则不会覆盖，默认值为0。这个选项能够避免在磁盘上存储过多的片，而且能够限制写入磁盘的最多片的数量。
```

注意，播放列表的`sequence number`对每个`segment`来说都必须是唯一的，而且它不能和片的文件名（当使用`wrap`选项时，文件名可能会重复使用）混淆。

## 分离视频音频流

```bash
# 分离视频流
ffmpeg -i input_file -vcodec copy -an output_file_video

# 分离音频流
ffmpeg -i input_file -acodec copy -vn output_file_audio
```

## 视频解复用

```bash
ffmpeg -i test.mp4 -vcoder copy -an -f m4v test.264
ffmpeg -i test.avi -vcoder copy -an -f m4v test.264
```

## 视频转码

```bash
# 转码为码流原始文件
ffmpeg -i test.mp4 -vcoder h264 -s 352*278 -an -f m4v test.264

# 转码为码流原始文件
ffmpeg -i test.mp4 -vcoder h264 -bf 0 -g 25 -s 352-278 -an -f m4v test.264

# 转码为封装文件 -bf B帧数目控制, -g 关键帧间隔控制, -s 分辨率控制
ffmpeg -i test.avi -vcoder mpeg4 -vtag xvid -qsame test_xvid.avi
```

## 视频封装

```bash
ffmpeg -i video_file -i audio_file -vcoder copy -acodec copy output_file
```

## 视频剪切

```bash
# 视频截图
ffmpeg -i test.avi -r 1 -f image2 image.jpeg

# 剪切视频 -r 提取图像频率， -ss 开始时间， -t 持续时间
ffmpeg -i input.avi -ss 0:1:30 -t 0:0:20 -vcoder copy -acoder copy output.avi
```

## 视频录制

```bash
ffmpeg -i rtsp://hostname/test -vcoder copy out.avi
```

## YUV序列播放

```bash
ffplay -f rawvideo -video_size 1920x1080 input.yuv
```

## YUV序列转AVI

```bash
ffmpeg -s w*h -pix_fmt yuv420p -i input.yuv -vcoder mpeg4 output.avi
```

### 常用参数说明

#### 主要参数

```bash
-i 设定输入流
-f 设定输出格式
-ss 开始时间
```

#### 视频参数

```bash
-b 设定视频流量，默认是200Kbit/s
-s 设定画面的宽和高
-aspect 设定画面的比例
-vn 不处理视频
-vcoder 设定视频的编码器，未设定时则使用与输入流相同的编解码器
```

### 音频参数

```bash
-ar 设定采样率
-ac 设定声音的Channel数
-acodec 设定沈阳的Channel数
-an 不处理音频
```

## 使用ffmpeg合并MP4文件

```bash
ffmpeg -i "Apache Sqoop Tutorial Part 1.mp4" -c copy -bsf:v h264_mp4toannexb -f mpegts intermediate1.ts
ffmpeg -i "Apache Sqoop Tutorial Part 2.mp4" -c copy -bsf:v h264_mp4toannexb -f mpegts intermediate2.ts
ffmpeg -i "Apache Sqoop Tutorial Part 3.mp4" -c copy -bsf:v h264_mp4toannexb -f mpegts intermediate3.ts
ffmpeg -i "Apache Sqoop Tutorial Part 4.mp4" -c copy -bsf:v h264_mp4toannexb -f mpegts intermediate4.ts
ffmpeg -i "concat:intermediate1.ts|intermediate2.ts|intermediate3.ts|intermediate4.ts" -c copy -bsf:a aac_adtstoasc "Apache Sqoop Tutorial.mp4"
```

## 使用ffmpeg转换flv到mp4

```bash
ffmpeg -i out.flv -vcodec copy -acodec copy out.mp4
```

## 视频添加水印

### 水印局中

```bash
ffmpeg -i out.mp4 -i sxyx2008@163.com.gif -filter_complex overlay="(main_w/2)-(overlay_w/2):(main_h/2)-(overlay_h)/2" output.mp4
```

参数解释

- -i out.mp4(视频源)
- -i sxyx2008@163.com.gif(水印图片)
- overlay 水印的位置
- output.mp4 输出文件

## 视频翻转和旋转

### 翻转

#### 水平翻转语法: -vf hflip

```bahs
ffplay -i out.mp4 -vf hflip
```

#### 垂直翻转语法：-vf vflip

```bash
ffplay -i out.mp4 -vf vflip
```

### 旋转

语法：`transpose={0,1,2,3}`

- 0:逆时针旋转90°然后垂直翻转
- 1:顺时针旋转90°
- 2:逆时针旋转90°
- 3:顺时针旋转90°然后水平翻转

### 将视频顺时针旋转90度

```bash
ffplay -i out.mp4 -vf transpose=1
```

### 将视频水平翻转(左右翻转)

```bash
ffplay -i out.mp4 -vf hflip
```

### 顺时针旋转90度并水平翻转

```bash
ffplay -i out.mp4 -vf transpose=1,hflip
```

## 添加字幕

有的时候你需要给视频加一个字幕(subtitle)，使用ffmpeg也可以做。一般我们见到的字幕以srt字幕为主，在ffmpeg里需要首先将srt字幕转化为ass字幕，然后就可以集成到视频中了(不是单独的字幕流，而是直接改写视频流)。

```bash
ffmpeg -i my_subtitle.srt my_subtitle.ass
ffmpeg -i inputfile.mp4 -vf ass=my_subtitle.ass outputfile.mp4
```

但是值得注意的是：

> `my_subtitle.srt`需要使用`UTF8`编码，老外不会注意到这一点，但是中文这是必须要考虑的；

将字幕直接写入视频流需要将每个字符渲染到画面上，因此有一个字体的问题，在`ass`文件中会指定一个缺省字体，例如`Arial`，但是我们首先需要让`ffmpeg`能找到字体文件，不然文字的渲染就无从谈起了。`ffmpeg`使用了`fontconfig`来设置字体配置。你需要首先设置一下`FONTCONFIG_PATH`或者`FONTCONFIG_FILE`环境变量，不然`fontconfig`是无法找到配置文件的，这一点请参看这篇文章，如果你设置的是`FONTCONFIG_PATH`，那把配置文件保存为`%FONTCONFIG_PATH%/font.conf`即可，然后你可以在`font.conf`文件中配置字体文件的路径之类的。

`Windows`下为`fontconfig`设置如下的环境变量

```bash
FC_CONFIG_DIR=C:\ffmpeg
FONTCONFIG_FILE=font.conf
FONTCONFIG_PATH=C:\ffmpeg
PATH=C:\ffmpeg\bin;%PATH%
```

下面是一个简单的`Windows`版`font.conf`文件。

```xml
<?xml version="1.0"?>
<fontconfig>

<dir>C:\WINDOWS\Fonts</dir>

<match target="pattern">
   <test qual="any" name="family"><string>mono</string></test>
   <edit name="family" mode="assign"><string>monospace</string></edit>
</match>

<match target="pattern">
   <test qual="all" name="family" mode="not_eq"><string>sans-serif</string></test>
   <test qual="all" name="family" mode="not_eq"><string>serif</string></test>
   <test qual="all" name="family" mode="not_eq"><string>monospace</string></test>
   <edit name="family" mode="append_last"><string>sans-serif</string></edit>
</match>

<alias>
   <family>Times</family>
   <prefer><family>Times New Roman</family></prefer>
   <default><family>serif</family></default>
</alias>
<alias>
   <family>Helvetica</family>
   <prefer><family>Arial</family></prefer>
   <default><family>sans</family></default>
</alias>
<alias>
   <family>Courier</family>
   <prefer><family>Courier New</family></prefer>
   <default><family>monospace</family></default>
</alias>
<alias>
   <family>serif</family>
   <prefer><family>Times New Roman</family></prefer>
</alias>
<alias>
   <family>sans</family>
   <prefer><family>Arial</family></prefer>
</alias>
<alias>
   <family>monospace</family>
   <prefer><family>Andale Mono</family></prefer>
</alias>
<match target="pattern">
   <test name="family" mode="eq">
      <string>Courier New</string>
   </test>
   <edit name="family" mode="prepend">
      <string>monospace</string>
   </edit>
</match>
<match target="pattern">
   <test name="family" mode="eq">
      <string>Courier</string>
   </test>
   <edit name="family" mode="prepend">
      <string>monospace</string>
   </edit>
</match>

</fontconfig>
```

下面这个是`Linux`系统下改版过来的

```xml
<?xml version="1.0"?>
<!DOCTYPE fontconfig SYSTEM "fonts.dtd">
<!-- /etc/fonts/fonts.conf file to configure system font access -->
<fontconfig>
<!-- 
    Find fonts in these directories
-->
<dir>C:/Windows/Fonts</dir>
<!--
<dir>/usr/X11R6/lib/X11/fonts</dir>
-->
<!--
    Accept deprecated 'mono' alias, replacing it with 'monospace'
-->
<match target="pattern">
    <test qual="any" name="family"><string>mono</string></test>
    <edit name="family" mode="assign"><string>monospace</string></edit>
</match>

<!--
    Load per-user customization file, but don't complain
    if it doesn't exist
-->
<include ignore_missing="yes" prefix="xdg">fontconfig/fonts.conf</include>

<!--
    Load local customization files, but don't complain
    if there aren't any
-->
<include ignore_missing="yes">conf.d</include>
<include ignore_missing="yes">local.conf</include>

<!--
    Alias well known font names to available TrueType fonts.
    These substitute TrueType faces for similar Type1
    faces to improve screen appearance.
-->
<alias>
    <family>Times</family>
    <prefer><family>Times New Roman</family></prefer>
    <default><family>serif</family></default>
</alias>
<alias>
    <family>Helvetica</family>
    <prefer><family>Arial</family></prefer>
    <default><family>sans</family></default>
</alias>
<alias>
    <family>Courier</family>
    <prefer><family>Courier New</family></prefer>
    <default><family>monospace</family></default>
</alias>

<!--
    Provide required aliases for standard names
    Do these after the users configuration file so that
    any aliases there are used preferentially
-->
<alias>
    <family>serif</family>
    <prefer><family>Times New Roman</family></prefer>
</alias>
<alias>
    <family>sans</family>
    <prefer><family>Arial</family></prefer>
</alias>
<alias>
    <family>monospace</family>
    <prefer><family>Andale Mono</family></prefer>
</alias>
</fontconfig>
```

参考：

- http://blog.raphaelzhang.com/2013/04/video-streaming-and-ffmpeg-transcoding/

## 嵌入字幕

在一个MP4文件里面添加字幕，不是把 .srt 字幕文件集成到 MP4 文件里，而是在播放器里选择字幕，这种集成字幕比较简单，速度也相当快

```bash
ffmpeg -i input.mp4 -i subtitles.srt -c:s mov_text -c:v copy -c:a copy output.mp4
```

希望字幕直接显示出来，其实也不难

```bash
ffmpeg -i subtitle.srt subtitle.ass
ffmpeg -i input.mp4 -vf ass=subtitle.ass output.mp4
```

参考：

- http://blog.neten.de/posts/2013/10/06/use-ffmpeg-to-burn-subtitles-into-the-video/

## 视频压缩命令
> 微信不允许传输超过25兆的视频，在这里设置分辨率800X400
> ```shell script
> ffmpeg -y -i video.mp4 -ss 00:00:00.0 -t 00:06:00.0 -ab 56k -ar 44100 -b:v 2200k -r 29.97 -s 480x800 out.mp4
> ```

## 参数说明
1. 通用选项
    ```shell script
    -L license
    -h 帮助
    -fromats 显示可用的格式，编解码的，协议的...
    -f fmt 强迫采用格式fmt
    -I filename 输入文件
    -y 覆盖输出文件
    -t duration 设置纪录时间 hh:mm:ss[.xxx]格式的记录时间也支持
    -ss position 搜索到指定的时间 [-]hh:mm:ss[.xxx]的格式也支持
    -title string 设置标题
    -author string 设置作者
    -copyright string 设置版权
    -comment string 设置评论
    -target type 设置目标文件类型(vcd,svcd,dvd) 所有的格式选项（比特率，编解码以及缓冲区大小）自动设置，只需要输入如下的就可以了：ffmpeg -i myfile.avi -target vcd /tmp/vcd.mpg
    -hq 激活高质量设置
    -itsoffset offset 设置以秒为基准的时间偏移，该选项影响所有后面的输入文件。该偏移被加到输入文件的时戳，定义一个正偏移意味着相应的流被延迟了 offset秒。 [-]hh:mm:ss[.xxx]的格式也支持
    ```
2. 视频选项
    ```shell script
    -b bitrate 设置比特率，缺省200kb/s
    -r fps 设置帧频 缺省25
    -s size 设置帧大小 格式为WXH 缺省160X128.下面的简写也可以直接使用：
    Sqcif 128X96 qcif 176X144 cif 252X288 4cif 704X576
    -aspect aspect 设置横纵比 4:3 16:9 或 1.3333 1.7777
    -croptop size 设置顶部切除带大小 像素单位
    -cropbottom size –cropleft size –cropright size
    -padtop size 设置顶部补齐的大小 像素单位
    -padbottom size –padleft size –padright size –padcolor color 设置补齐条颜色(hex,6个16进制的数，红:绿:兰排列，比如 000000代表黑色)
    -vn 不做视频记录
    -bt tolerance 设置视频码率容忍度kbit/s
    -maxrate bitrate设置最大视频码率容忍度
    -minrate bitreate 设置最小视频码率容忍度
    -bufsize size 设置码率控制缓冲区大小
    -vcodec codec 强制使用codec编解码方式。如果用copy表示原始编解码数据必须被拷贝。
    -sameq 使用同样视频质量作为源（VBR）
    -pass n 选择处理遍数（1或者2）。两遍编码非常有用。第一遍生成统计信息，第二遍生成精确的请求的码率
    -passlogfile file 选择两遍的纪录文件名为file
    ```
3. 高级视频选项
    ```shell script
    -g gop_size 设置图像组大小
    -intra 仅适用帧内编码
    -qscale q 使用固定的视频量化标度(VBR)
    -qmin q 最小视频量化标度(VBR)
    -qmax q 最大视频量化标度(VBR)
    -qdiff q 量化标度间最大偏差 (VBR)
    -qblur blur 视频量化标度柔化(VBR)
    -qcomp compression 视频量化标度压缩(VBR)
    -rc_init_cplx complexity 一遍编码的初始复杂度
    -b_qfactor factor 在p和b帧间的qp因子
    -i_qfactor factor 在p和i帧间的qp因子
    -b_qoffset offset 在p和b帧间的qp偏差
    -i_qoffset offset 在p和i帧间的qp偏差
    -rc_eq equation 设置码率控制方程 默认tex^qComp
    -rc_override override 特定间隔下的速率控制重载
    -me method 设置运动估计的方法 可用方法有 zero phods log x1 epzs(缺省) full
    -dct_algo algo 设置dct的算法 可用的有 0 FF_DCT_AUTO 缺省的DCT 1 FF_DCT_FASTINT 2 FF_DCT_INT 3 FF_DCT_MMX 4 FF_DCT_MLIB 5 FF_DCT_ALTIVEC
    -idct_algo algo 设置idct算法。可用的有 0 FF_IDCT_AUTO 缺省的IDCT 1 FF_IDCT_INT 2 FF_IDCT_SIMPLE 3 FF_IDCT_SIMPLEMMX 4 FF_IDCT_LIBMPEG2MMX 5 FF_IDCT_PS2 6 FF_IDCT_MLIB 7 FF_IDCT_ARM 8 FF_IDCT_ALTIVEC 9 FF_IDCT_SH4 10 FF_IDCT_SIMPLEARM
    -er n 设置错误残留为n 1 FF_ER_CAREFULL 缺省 2 FF_ER_COMPLIANT 3 FF_ER_AGGRESSIVE 4 FF_ER_VERY_AGGRESSIVE
    -ec bit_mask 设置错误掩蔽为bit_mask,该值为如下值的位掩码 1 FF_EC_GUESS_MVS (default=enabled) 2 FF_EC_DEBLOCK (default=enabled)
    -bf frames 使用frames B 帧，支持mpeg1,mpeg2,mpeg4
    -mbd mode 宏块决策 0 FF_MB_DECISION_SIMPLE 使用mb_cmp 1 FF_MB_DECISION_BITS 2 FF_MB_DECISION_RD
    -4mv 使用4个运动矢量 仅用于mpeg4
    -part 使用数据划分 仅用于mpeg4
    -bug param 绕过没有被自动监测到编码器的问题
    -strict strictness 跟标准的严格性
    -aic 使能高级帧内编码 h263+
    -umv 使能无限运动矢量 h263+
    -deinterlace 不采用交织方法
    -interlace 强迫交织法编码仅对mpeg2和mpeg4有效。当你的输入是交织的并且你想要保持交织以最小图像损失的时候采用该选项。可选的方法是不交织，但是损失更大
    -psnr 计算压缩帧的psnr
    -vstats 输出视频编码统计到vstats_hhmmss.log
    -vhook module 插入视频处理模块 module 包括了模块名和参数，用空格分开
    ```
4. 音频选项
    ```shell script
    -ab bitrate 设置音频码率
    -ar freq 设置音频采样率
    -ac channels 设置通道 缺省为1
    -an 不使能音频纪录
    -acodec codec 使用codec编解码
    ```
5. 音频/视频捕获选项
    ```shell script
    -vd device 设置视频捕获设备。比如/dev/video0
    -vc channel 设置视频捕获通道 DV1394专用
    -tvstd standard 设置电视标准 NTSC PAL(SECAM)
    -dv1394 设置DV1394捕获
    -av device 设置音频设备 比如/dev/dsp
    ```
6. 高级选项
    ```shell script
    -map file:stream 设置输入流映射
    -debug 打印特定调试信息
    -benchmark 为基准测试加入时间
    -hex 倾倒每一个输入包
    -bitexact 仅使用位精确算法 用于编解码测试
    -ps size 设置包大小，以bits为单位
    -re 以本地帧频读数据，主要用于模拟捕获设备
    -loop 循环输入流（只工作于图像流，用于ffserver测试）
    ```
   
## [用ffmpeg命令行转压视频](https://segmentfault.com/a/1190000002502526)
```shell script
ffmpeg -i MVI_7274.MOV -vcodec libx264 -preset fast -crf 20 -y -vf "scale=1920:-1" -acodec libmp3lame -ab 128k a.mp4
```
### -preset
`-preset`：指定编码的配置。x264编码算法有很多可供配置的参数，不同的参数值会导致编码的速度大相径庭，甚至可能影响质量。
为了免去用户了解算法，然后手工配置参数的麻烦。x264提供了一些预设值，而这些预设值可以通过preset指定。
这些预设值有包括：`ultrafast`，`superfast`，`veryfast`，`faster`，`fast`，`medium`，`slow`，`slower`，`veryslow`和`placebo`。`ultrafast`编码速度最快，但压缩率低，
生成的文件更大，`placebo`则正好相反。`x264`所取的默认值为`medium`。需要说明的是，`preset`主要是影响编码的速度，并不会很大的影响编码出来的结果的质量。
压缩高清电影时，我一般用`slow`或者`slower`，当你的机器性能很好时也可以使用`veryslow`，不过一般并不会带来很大的好处。

### -crf
`-crf`：这是最重要的一个选项，用于指定输出视频的质量，取值范围是`0-51`，默认值为`23`，数字越小输出视频的质量越高。
这个选项会直接影响到输出视频的码率。一般来说，压制`480p`我会用20左右，压制`720p`我会用`16-18`，`1080p`我没尝试过。
个人觉得，一般情况下没有必要低于16。最好的办法是大家可以多尝试几个值，每个都压几分钟，看看最后的输出质量和文件大小，自己再按需选择。

其实还有`-b 1024k`这样的参数，但是我发现-crf设置上后-b就不管用了。根据我自己的简单尝试，压制5D2拍摄的一段18秒`1920x1080`的视频（下午自然光、图像简单、大面积白墙、只有一扇黑门）
crf和压出来的文件大小关系如下：

| crf | 文件大小|
| ---- | ---- |
| 16 | 54M |
| 18 | 39M  |
| 20  | 25M  |
| 22	| 17M |
| 24	| 11M |
| 26	| 7.3M |
| 28	| 5.0M |
| 30	| 3.6M |
| 32	| 2.7M |
默认	14M（crf为23）
又比较了一下crf在20、28、32时的视频质量，发现32还是能看出质量下降的，20的确非常精细，但28跟20之间的差别并不是那么大，crf值设置在26-28之间比较好。如果对尺寸有要求，
什么都不设，用默认的也行（可能是31）。

另外，关于`preset`，`slow`和`fast`只跟运行时间有关，`slow`跑的时间比`fast`长不少，`slow`出来的mp4文件会小一些（12M），`fast`出来的文件会大一些（14M），但视频质量的差距并不明显。

如果把原视频尺寸从1920x1080缩小到960x540，则视频尺寸变为了：

| crf	| 文件大小 |
| ---- | ---- |
| 16	| 11M |
| 18	| 6.7M |
| 20	| 4.4M |
| 22	| 3.0M |
| 24	| 2.1M |
| 26	| 1.6M |
| 28	| 1.3M |
| 30	| 1.1M |
| 32	| 893K |
默认	2.5M（crf为23）
综上，对质量要求较高时，选22以下；对尺寸要求非常高时，选26（但质量确实是会稍差一些），否则选24的性价比比较高（或者默认的23也行），如果对尺寸实在要求非常非常高，那就28以上吧。

### -threads
`-threads` n 来实施多线程的运算，充分利用多核cpu