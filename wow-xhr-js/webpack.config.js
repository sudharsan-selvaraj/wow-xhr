const path = require('path');
module.exports = {

    mode: 'production',

    entry: './src/index.ts',

    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js',
    },

    resolve: {
        extensions: ['.ts', '.js'],
    },

    module: {
        rules: [
            {
                test: /\.tsx?/,
                use: {
                    loader: 'ts-loader',
                    options: {
                        transpileOnly: true,
                    }
                },
                exclude: /node_modules/,
            },
            {
                test: require.resolve('xhook'),
                use: [{
                    loader: 'expose-loader',
                    options: {
                        exposes: {
                            globalName: 'xhook',
                            override: true
                        },
                    }
                }]
            }
        ]
    }
};