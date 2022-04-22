const path = require('path');

// 1. 导入 HTML 插件，得到一个构造函数
const HtmlPlugin = require('html-webpack-plugin');

// 2. 创建 HTML 插件的实例对象
const htmlPlugin = new HtmlPlugin({
    template: './index.html',       // 指定原文件的存放路径
    filename: path.join(__dirname,'dist/index.html')   // 指定生成文件的存放路径
});

module.exports = {
    mode: 'development',  // development, production
    // 指定打包的入口
    entry: "./src/index.js",
    // 指定打包的出口
    output: {
        filename: "main.js",
        // 表示输出文件的存放路径
        path: path.join(__dirname,'dist')
        // path: path.resolve(__dirname,'dist')
    },
    plugins: [htmlPlugin], // 3. 通过plugins节点，使 htmlPlugin 插件生效
}