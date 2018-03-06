#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

attribute LOW vec2 a_position;
attribute LOW float a_alpha;
uniform HIGH mat4 u_projection;
uniform HIGH mat4 u_transform;
uniform LOW float u_alpha;

varying LOW vec2 v_position;
varying LOW float v_alpha;

void main()
{
	v_position = a_position;
	v_alpha = a_alpha * u_alpha;
    gl_Position =  u_projection * u_transform * vec4 ( a_position , 0.0 , 1.0 );
}