'use strict';

var path = require('path');

module.exports = function (grunt) {
	require('load-grunt-tasks')(grunt);
	require('time-grunt')(grunt);

	grunt.initConfig({
		watch: {
			compass: {
				files: ['src/main/scss/{,*/}*.{scss,sass}'],
				tasks: ['compass:server', 'autoprefixer']
			},
			styles: {
				files: ['src/main/webapp/styles/{,*/}*.css'],
				tasks: ['copy:styles', 'autoprefixer']
			}
		},
		autoprefixer: {
			options: ['last 1 version'],
			dist: {
				files: [{
					expand: true,
					cwd: '.tmp/styles/',
					src: '{,*/}*.css',
					dest: '.tmp/styles/'
				}]
			}
		},
		jshint: {
			options: {
				jshintrc: '.jshintrc'
			},
			all: [
				'Gruntfile.js',
				'src/main/webapp/scripts/{,*/}*.js'
			]
		},
		compass: {
			options: {
				sassDir: 'src/main/scss',
				cssDir: 'src/main/webapp/styles',
				generatedImagesDir: '.tmp/images/generated',
				imagesDir: 'src/main/webapp/images',
				javascriptsDir: 'src/main/webapp/scripts',
				fontsDir: 'src/main/webapp/styles/fonts',
				importPath: 'src/main/webapp/lib',
				httpImagesPath: '/images',
				httpGeneratedImagesPath: '/images/generated',
				httpFontsPath: '/styles/fonts',
				relativeAssets: false
			},
			dist: {},
			server: {
				options: {
					debugInfo: true
				}
			}
		},
		// Put files not handled in other tasks here
		copy: {
			styles: {
				expand: true,
				cwd: 'src/main/webapp/styles',
				dest: '.tmp/styles/',
				src: '{,*/}*.css'
			}
		},
		concurrent: {
			server: [
				'compass:server',
				'copy:styles'
			],
			dist: [
				'compass:dist',
				'copy:styles',
			]
		},
		bowerInstall: {
			target: {
				src: [
					'src/main/webapp/index.html'
				]
			}
		},
		injector: {
			options: {
				transform : function(filepath) {
					var e = path.extname(filepath).slice(1);
					filepath = filepath.substring('/src/main/webapp/'.length); // remove base webapp path
					if (e === 'css') {
						return '<link rel="stylesheet" href="' + filepath + '">';
					} else if (e === 'js') {
						return '<script charset="UTF-8" src="' + filepath + '"></script>';
					} else if (e === 'html') {
						return '<link rel="import" href="' + filepath + '">';
					}
				}
			},
			local_dependencies: {
				files: {
					'src/main/webapp/index.html': ['src/main/webapp/js/controllers/**/*.js']
				}
			}
		}
	});

	grunt.registerTask('build', [
		'bowerInstall',
		'injector',
		'concurrent:dist',
		'autoprefixer',
		'copy:dist',
	]);

	grunt.registerTask('default', [
		'build'
	]);
};