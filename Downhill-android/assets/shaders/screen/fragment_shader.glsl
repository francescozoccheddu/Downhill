#ifdef GL_ES 
#define LOW lowp
#else
#define LOW
#endif

uniform LOW float u_alpha;
uniform LOW sampler2D u_texture;
varying LOW vec2 v_texCoord;

void main()
{
	LOW vec4 sample = texture2D ( u_texture , v_texCoord );
	gl_FragColor = vec4 ( sample.rgb , sample.a * u_alpha );
}
