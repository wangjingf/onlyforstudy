module.exports = {
    devServer:{
        proxy:{
            "/cxf3.1":{
                target:"http://localhost:8090"
            }
        }
    },
    configureWebpack: {
        resolve: {
            alias: {
                vue$: "vue/dist/vue.esm.js"
            }
        }
    }
}