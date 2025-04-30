const { defineConfig } = require('@vue/cli-service')
const path = require('path');

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: './', // 添加这行，使用相对路径
  devServer: {
    // disableHostCheck: true,
    allowedHosts: 'all',
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://localhost:8088',
        //target: 'http://374e506b.r3.cpolar.cn/greenlifehouduantest',
        changeOrigin: true,
        secure: false, // 如果你需要代理到非HTTPS后端
        publicPath: './',
    outputDir: 'dist', // build时构建文件的目录 构建时传入 --no-clean 可关闭该行为
    assetsDir: 'assets', // build时放置生成的静态资源（js、css、img、fonts）的（相对于 outputDir 的）目录
    filenameHashing: true, // 默认在生成的静态资源文件名中包含hash以控制缓存
    runtimeCompiler: false, // 是否使用包含运行时编译器的 Vue 构建版本
    productionSourceMap: false // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建
      }
    }
  }
});













// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })
// // module.exports = {
// //   devServer: {
// //     proxy: {
// //       '/api': {
// //         target: 'http://localhost:8082', // 后端服务器的URL
// //         changeOrigin: true,
// //         pathRewrite: { '^/api': '' }
// //       }
// //     }
// //   }
// // }
// const path = require('path');

// module.exports = {
//   configureWebpack: {
//     resolve: {
//       alias: {
//         '@': path.resolve(__dirname, 'src')
//       }
//     }
//   }
// };
