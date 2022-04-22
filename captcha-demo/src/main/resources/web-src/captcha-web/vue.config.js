module.exports = {
    // 基本路径
    publicPath: "./",
    // 构建时的输出目录
    outputDir: "static",
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8081',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/'
                }
            }
        }
    }
}