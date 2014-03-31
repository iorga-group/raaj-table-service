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
			},
			js: {
				files: ['src/main/webapp/scripts/**/{,*/}*.js'],
				tasks: ['jshint:all']
			},
			htmlpartial: {
				files: ['src/main/webapp/templates/**/{,*/}*.html'],
				tasks: ['htmlhint:partial']
			},
			htmlfull: {
				files: ['src/main/webapp/index.html'],
				tasks: ['htmlhint:full']
			},
		},
		// Adds vendor prefixes in CSS when needed
		autoprefixer: {
			options: ['last 1 version'],
			dist: {
				files: [{
					expand: true,
					src: 'src/main/webapp/styles/{,*/}*.css',
				}]
			}
		},
		// Uses JSHint code quality tool
		jshint: {
			options: {
				jshintrc: '.jshintrc'
			},
			all: [
				'src/main/webapp/scripts/**/{,*/}*.js'
			]
		},
		// Uses HTMLHint code quality tool
		htmlhint: {
		    partial: {
		        options: {
		            // Force tags to have a closing pair
		            'tag-pair': true,
		            // Force tags to be lowercase
		            'tagname-lowercase': true,
		            // Force attribute names to be lowercase
		            'attr-lowercase': true,
		            // Force attributes to have double quotes rather than single
		            'attr-value-double-quotes': true,
		            // Force special characters to be escaped
		            'spec-char-escape': true,
		            // Prevent using the same ID multiple times in a document
		            'id-unique': true,
		            // Prevent script tags being loaded in the head for performance reasons
		            'head-script-disabled': true,
		            // Prevent style tags. CSS should be loaded through includes
		            'style-disabled': true
		        },
		        src: ['src/main/webapp/templates/**/*.html']
		    },
		    full: {
		        options: {
		            // Force tags to have a closing pair
		            'tag-pair': true,
		            // Force tags to be lowercase
		            'tagname-lowercase': true,
		            // Force attribute names to be lowercase
		            'attr-lowercase': true,
		            // Force attributes to have double quotes rather than single
		            'attr-value-double-quotes': true,
		            // Force the DOCTYPE declaration to come first in the document
		            'doctype-first': true,
		            // Force special characters to be escaped
		            'spec-char-escape': true,
		            // Prevent using the same ID multiple times in a document
		            'id-unique': true,
		            // Prevent script tags being loaded in the head for performance reasons
		            'head-script-disabled': true,
		            // Prevent style tags. CSS should be loaded through includes
		            'style-disabled': true
		        },
		        src: ['src/main/webapp/index.html']
		    }
		},
		// Uses Compass
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
		// Cleans unnecessary files and folders
	    clean: {
	    	server: '.tmp'
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
		// Runs multiple tasks asynchronously
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
		// Install Bower dependencies
		bower: {
			install: {
				options: {
					bowerOptions: {
						forceLatest: true
					}
				}
			}
		},
		// Injects Bower dependencies directly in index.html
		bowerInstall: {
			target: {
				src: [
					'src/main/webapp/index.html'
				]
			}
		},
		// Injects AngularJS files created in the scripts/controllers folder directly in index.html
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
					'src/main/webapp/index.html': ['src/main/webapp/scripts/controllers/**/*.js']
				}
			}
		},
	    // Reads HTML for usemin blocks to enable smart builds that automatically
	    // concat, minify and revision files. Creates configurations in memory so
	    // additional tasks can operate on them
		useminPrepare: {
			html: ['src/main/webapp/*.html'],
			options: {
				dest: 'src/main/webapp/dist'
			}
		},
		// Performs rewrites based on the useminPrepare configuration
		usemin: {
			html: ['src/main/webapp/{,*/}*.html'],
			css: ['src/main/webapp/styles/{,*/}*.css'],
			options: {
				assetsDirs: ['src/main/webapp/dist']
			}
		},
	    // Allow the use of non-minsafe AngularJS files. Automatically makes it
	    // minsafe compatible so Uglify does not destroy the ng references
	    ngmin: {
	    	dist: {
		        files: [{
		        	expand: true,
		        	src: 'src/main/webapp/scripts/**/*.js'
		        }]
	    	}
	    }
	});
	
	grunt.registerTask('serve', [
		'clean:server',
		'bower',
		'bowerInstall',
		'injector',
	    'concurrent:server'
	]);

	grunt.registerTask('build', [
	    'clean:server',
	    'bower',
		'bowerInstall',
		'injector',
		'useminPrepare',
		'concurrent:dist',
		'autoprefixer',
		'ngmin',
		'concat',
		'uglify',
		//'copy:dist',
		'usemin'
	]);

	grunt.registerTask('default', [
		'build'
	]);
};