#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

attribute HIGH vec2 a_position;
attribute LOW float a_size;
attribute LOW float a_opacity;

uniform LOW float u_width;
uniform HIGH mat4 u_projection;

varying LOW float v_opacity;

void main()
{
	v_opacity = a_opacity;
	HIGH vec4 pos = u_projection * vec4 ( a_position , 0.0 , 1.0 );
    gl_Position = pos;
	gl_PointSize = a_size * u_width / pos.w;
}