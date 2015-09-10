# Require any additional compass plugins here.

# Set this to the root of your project when deployed:
css_dir = "src/main/resources/static/css"
sass_dir = "src/main/scss"
#images_dir = "src/main/webapp/main/assets/images"
#javascripts_dir = "src/main/webapp/main/assets/js"

# You can select your preferred output style here (can be overridden via the command line):
# output_style = :expanded or :nested or :compact or :compressed
output_style = (environment == :production) ? :compressed : :expanded

sass_options = {:sourcemap => true, :debug_info => false}
enable_sourcemaps = true

disable_warnings = true

# To enable relative paths to assets via compass helper functions. Uncomment:
relative_assets = true

# disable the asset cache buster:
asset_cache_buster :none

# To disable debugging comments that display the original location of your selectors. Uncomment:
line_comments = true

# Color output
color_output = true

# If you prefer the indented syntax, you might want to regenerate this
# project again passing --syntax sass, or you can uncomment this:
# preferred_syntax = :sass
# and then run:
# sass-convert -R --from scss --to sass sass scss && rm -rf sass && mv scss sass

# Make a copy of sprites with a name that has no uniqueness of the hash.
on_sprite_saved do |filename|
  if File.exists?(filename)
    FileUtils.mv filename, filename.gsub(%r{-s[a-z0-9]{10}\.png$}, '.png')
  end
end

# Replace in stylesheets generated references to sprites
# by their counterparts without the hash uniqueness.
on_stylesheet_saved do |filename|
  if File.exists?(filename)
    css = File.read filename
    File.open(filename, 'w+') do |f|
      f << css.gsub(%r{-s[a-z0-9]{10}\.png}, '.png')
    end
  end
end
