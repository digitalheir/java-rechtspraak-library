var StaticSiteGeneratorPlugin = require('static-site-generator-webpack-plugin');
var data = require('./data');
var basscss = require('postcss-basscss');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var precss = require('precss');
var autoprefixer = require('autoprefixer');
var scss = require('postcss-scss');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var path = require('path');


module.exports = {
    entry: {
        main: './src/entry.js',
        css: './src/css/base.scss'
    },
    output: {
        filename: 'bundle.js',
        path: "dist",
        libraryTarget: 'umd'
    },

    module: {
        loaders: [
            {test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader'},
            {test: /\.jsx$/, exclude: /node_modules/, loader: 'babel-loader'},
            {
                test: /\.scss$/, loader: ExtractTextPlugin.extract("style-loader", "css-loader!postcss-loader")
            },
            {test: /\.jpe?g$|\.gif$|\.png$|\.svg$|\.woff$|\.ttf$|\.wav$|\.mp3$/, loader: "file"}
        ]
    },
    plugins: [
        new CopyWebpackPlugin([
            {from: 'src/public'}
        ]),
        new StaticSiteGeneratorPlugin('bundle.js', data.routes, data),
        new ExtractTextPlugin('style.css')
    ],
    postcss: function () {
        return [basscss, precss, autoprefixer];
    },
    context: path.join(__dirname),
    cssnext: {
        compress: true,
        features: {
            rem: false,
            pseudoElements: false,
            colorRgba: false
        }
    }
};
