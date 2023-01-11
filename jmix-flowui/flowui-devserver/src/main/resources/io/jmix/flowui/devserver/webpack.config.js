/*
 * Copyright 2022 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file contains project specific customizations for the webpack build.
 * It is autogenerated if it didn't exist or if it was made for an older
 * incompatible version.
 *
 * Defaults are provided in an autogenerated webpack.generated.js file and used by this file.
 * The webpack.generated.js file is overwritten on each build and no customization can be done there.
 */
const merge = require('webpack-merge');
const flowDefaults = require('./webpack.generated.js');

/**
 * To change the webpack config, add a new configuration object in
 * the merge arguments below:
 */
module.exports = merge(flowDefaults,
  // Override default configuration
  // {
  //   mode: 'development',
  //   devtool: 'inline-source-map',
  // },

  // Add a custom plugin
  // (install the plugin with `npm install --save-dev webpack-bundle-analyzer`)
  // {
  //   plugins: [
  //     new require('webpack-bundle-analyzer').BundleAnalyzerPlugin({
  //       analyzerMode: 'static'
  //     })
  //   ]
  // },
);