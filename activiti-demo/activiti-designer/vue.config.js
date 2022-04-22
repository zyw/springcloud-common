var path = require('path');

module.exports = {
    configureWebpack: {
      module: {
        rules: [
          {
            test: /\.bpmn$/,
            use: 'raw-loader',
          },
        ],
      },
    },
  };